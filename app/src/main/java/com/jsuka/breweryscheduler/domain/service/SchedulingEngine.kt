package com.jsuka.breweryscheduler.domain.service

import com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity
import com.jsuka.breweryscheduler.data.repository.ScheduleRepository
import com.jsuka.breweryscheduler.data.repository.WriteResult
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import java.time.LocalDate

/** What happened when a supervisor tried to place someone on a shift. */
sealed interface AssignmentOutcome {

    data class Valid(val assignmentId: Long) : AssignmentOutcome

    data class Conflict(
        val assignmentId: Long,
        val failedRules: List<String>,
        val reason: String
    ) : AssignmentOutcome

    data class Rejected(val reason: String) : AssignmentOutcome

    /** Someone else changed this assignment first; the caller must refresh. */
    data object StaleVersion : AssignmentOutcome
}

/**
 * Creates schedule assignments and delegates their validation to
 * [ConflictValidator], which is the division of responsibility set out in
 * the Module 3 design and formalized in the Module 4 class diagram.
 *
 * An assignment moves Draft, then PendingValidation, then either Valid or
 * Conflict, following the state machine in Module 4 Figure 2.
 */
class SchedulingEngine(
    private val schedules: ScheduleRepository,
    private val contextBuilder: AssignmentContextBuilder,
    private val validator: ConflictValidator = ConflictValidator()
) {

    suspend fun createAssignment(
        request: AssignmentRequest,
        evaluationDate: LocalDate = LocalDate.now()
    ): AssignmentOutcome {
        val proposal = contextBuilder.build(request, evaluationDate)
            ?: return AssignmentOutcome.Rejected("Employee, position or shift could not be found")

        // Draft, then persisted and moved to PendingValidation.
        val assignmentId = schedules.addAssignment(
            ScheduleAssignmentEntity(
                scheduleId = request.scheduleId,
                personId = request.personId,
                jobPositionId = request.jobPositionId,
                shiftId = request.shiftId,
                state = AssignmentState.PENDING_VALIDATION
            )
        )

        val report = validator.validate(proposal.copy(assignmentId = assignmentId))
        val saved = schedules.assignmentById(assignmentId)
            ?: return AssignmentOutcome.Rejected("Assignment could not be read back after saving")

        val newState = if (report.isValid) AssignmentState.VALID else AssignmentState.CONFLICT

        return when (schedules.applyValidation(saved, newState, report.summary())) {
            is WriteResult.Success ->
                if (report.isValid) {
                    AssignmentOutcome.Valid(assignmentId)
                } else {
                    AssignmentOutcome.Conflict(
                        assignmentId = assignmentId,
                        failedRules = report.failedRuleNames,
                        reason = report.summary() ?: "Assignment is in conflict"
                    )
                }

            is WriteResult.StaleVersion -> AssignmentOutcome.StaleVersion

            is WriteResult.Failed -> AssignmentOutcome.Rejected("Assignment could not be saved")
        }
    }

    /**
     * Re-runs validation on an existing assignment, used after a supervisor
     * edits it out of the Conflict state.
     */
    suspend fun revalidate(
        assignmentId: Long,
        evaluationDate: LocalDate = LocalDate.now()
    ): AssignmentOutcome {
        val existing = schedules.assignmentById(assignmentId)
            ?: return AssignmentOutcome.Rejected("Assignment no longer exists")

        val proposal = contextBuilder.build(
            AssignmentRequest(
                scheduleId = existing.scheduleId,
                personId = existing.personId,
                jobPositionId = existing.jobPositionId,
                shiftId = existing.shiftId,
                assignmentId = existing.id
            ),
            evaluationDate
        ) ?: return AssignmentOutcome.Rejected("Assignment context could not be rebuilt")

        val report = validator.validate(proposal)
        val newState = if (report.isValid) AssignmentState.VALID else AssignmentState.CONFLICT

        return when (schedules.applyValidation(existing, newState, report.summary())) {
            is WriteResult.Success ->
                if (report.isValid) AssignmentOutcome.Valid(assignmentId)
                else AssignmentOutcome.Conflict(assignmentId, report.failedRuleNames, report.summary()!!)
            is WriteResult.StaleVersion -> AssignmentOutcome.StaleVersion
            is WriteResult.Failed -> AssignmentOutcome.Rejected("Assignment could not be saved")
        }
    }

    /**
     * Supervisor override. Module 4 grants OverrideValidation only to the
     * Supervisor class, so the caller is responsible for checking the role
     * before invoking this.
     */
    suspend fun overrideConflict(assignmentId: Long): AssignmentOutcome {
        val existing = schedules.assignmentById(assignmentId)
            ?: return AssignmentOutcome.Rejected("Assignment no longer exists")

        if (existing.state != AssignmentState.CONFLICT) {
            return AssignmentOutcome.Rejected("Only an assignment in conflict can be overridden")
        }

        return when (
            schedules.applyValidation(
                existing,
                AssignmentState.VALID,
                "Overridden by supervisor: ${existing.conflictReason.orEmpty()}"
            )
        ) {
            is WriteResult.Success -> AssignmentOutcome.Valid(assignmentId)
            is WriteResult.StaleVersion -> AssignmentOutcome.StaleVersion
            is WriteResult.Failed -> AssignmentOutcome.Rejected("Override could not be saved")
        }
    }
}
