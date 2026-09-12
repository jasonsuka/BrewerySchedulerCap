package com.jsuka.breweryscheduler.domain.service

import com.jsuka.breweryscheduler.data.local.entity.fullName
import com.jsuka.breweryscheduler.data.repository.DepartmentRepository
import com.jsuka.breweryscheduler.data.repository.EmployeeRepository
import com.jsuka.breweryscheduler.data.repository.QualificationRepository
import com.jsuka.breweryscheduler.data.repository.ScheduleRepository
import com.jsuka.breweryscheduler.domain.model.AssignedShift
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import java.time.LocalDate

/** A supervisor's request to place one employee on one shift. */
data class AssignmentRequest(
    val scheduleId: Long,
    val personId: Long,
    val jobPositionId: Long,
    val shiftId: Long,
    val assignmentId: Long = 0
)

/**
 * Resolves an [AssignmentRequest] into the fully populated
 * [ProposedAssignment] the rules evaluate. Keeping this assembly separate
 * is what allows the rules themselves to stay free of data-access code.
 */
class AssignmentContextBuilder(
    private val employees: EmployeeRepository,
    private val departments: DepartmentRepository,
    private val qualifications: QualificationRepository,
    private val schedules: ScheduleRepository
) {

    suspend fun build(
        request: AssignmentRequest,
        evaluationDate: LocalDate = LocalDate.now()
    ): ProposedAssignment? {
        val person = employees.getById(request.personId) ?: return null
        val position = departments.positionById(request.jobPositionId) ?: return null
        val department = departments.getById(position.departmentId) ?: return null
        val shift = schedules.shiftById(request.shiftId) ?: return null

        val homeDepartment = person.homeDepartmentId?.let { departments.getById(it) }

        // Everything the person already holds, sorted by start time so the
        // overlap check can stop at the first collision.
        val existing = schedules.assignmentsForPerson(person.id)
            .filter { it.state != AssignmentState.CONFLICT }
            .mapNotNull { assignment ->
                schedules.shiftById(assignment.shiftId)?.let { s ->
                    AssignedShift(
                        shiftId = s.id,
                        date = s.date,
                        startMinute = s.startMinute,
                        endMinute = s.endMinute
                    )
                }
            }
            .sortedBy { it.startMinute }

        val remaining = remainingInHomeDepartment(person.homeDepartmentId, person.id, shift.date)

        return ProposedAssignment(
            assignmentId = request.assignmentId,
            scheduleId = request.scheduleId,
            personId = person.id,
            personName = person.fullName,
            jobPositionId = position.id,
            positionTitle = position.title,
            requiredCredentialCode = position.requiredCredentialCode,
            departmentId = department.id,
            departmentName = department.name,
            shiftId = shift.id,
            shiftDate = shift.date,
            shiftStartMinute = shift.startMinute,
            shiftEndMinute = shift.endMinute,
            credentials = qualifications.credentialMapFor(person.id),
            availability = employees.availabilityFor(person.id),
            existingAssignments = existing,
            homeDepartmentName = homeDepartment?.name,
            homeDepartmentMinimum = homeDepartment?.minimumStaffing ?: 0,
            homeDepartmentRemainingIfAssigned = remaining,
            evaluationDate = evaluationDate
        )
    }

    /**
     * How many of the home department's employees would still be available
     * on the shift date if this person were pulled away. Anyone already
     * assigned outside the department that day is not counted.
     */
    private suspend fun remainingInHomeDepartment(
        homeDepartmentId: Long?,
        movingPersonId: Long,
        date: LocalDate
    ): Int {
        if (homeDepartmentId == null) return 0
        val roster = employees.inDepartment(homeDepartmentId)
        var available = 0
        for (member in roster) {
            if (member.id == movingPersonId) continue
            val assignedAway = schedules.assignmentsForPerson(member.id).any { assignment ->
                val shift = schedules.shiftById(assignment.shiftId)
                val position = departments.positionById(assignment.jobPositionId)
                shift?.date == date &&
                    position != null &&
                    position.departmentId != homeDepartmentId
            }
            if (!assignedAway) available++
        }
        return available
    }
}
