package com.jsuka.breweryscheduler.data.repository

import com.jsuka.breweryscheduler.data.local.dao.ScheduleDao
import com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity
import com.jsuka.breweryscheduler.data.local.entity.ScheduleEntity
import com.jsuka.breweryscheduler.data.local.entity.ShiftEntity
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Outcome of a write that is guarded by an optimistic concurrency check.
 * [StaleVersion] is what the UI turns into a refresh prompt, which is the
 * behavior specified in the Module 5 fault-tolerance plan.
 */
sealed interface WriteResult {
    data object Success : WriteResult
    data object StaleVersion : WriteResult
    data class Failed(val reason: String) : WriteResult
}

class ScheduleRepository(private val dao: ScheduleDao) : IRepository<ScheduleEntity> {

    override suspend fun add(item: ScheduleEntity): Long = dao.insert(item)

    /**
     * Schedule state changes always go through the version-guarded query,
     * so this override delegates rather than issuing a blind update.
     */
    override suspend fun update(item: ScheduleEntity): Boolean =
        dao.updateStateIfCurrent(item.id, item.state, item.version) > 0

    override suspend fun delete(item: ScheduleEntity): Boolean = dao.delete(item) > 0

    override suspend fun getById(id: Long): ScheduleEntity? = dao.findById(id)

    override suspend fun getAll(): List<ScheduleEntity> = dao.findAll()

    suspend fun forWeek(weekStart: LocalDate): ScheduleEntity? = dao.findByWeek(weekStart)

    /**
     * Returns the schedule for a week, creating an empty draft the first
     * time a supervisor opens it.
     */
    suspend fun getOrCreateForWeek(weekStart: LocalDate): ScheduleEntity {
        dao.findByWeek(weekStart)?.let { return it }
        val id = dao.insert(
            ScheduleEntity(weekStartDate = weekStart, state = ScheduleState.DRAFT)
        )
        return dao.findById(id)!!
    }

    /** Moves a draft into review, which is the state publication acts on. */
    suspend fun submitForReview(schedule: ScheduleEntity): WriteResult {
        val rows = dao.updateStateIfCurrent(
            id = schedule.id,
            newState = ScheduleState.UNDER_REVIEW,
            expectedVersion = schedule.version
        )
        return if (rows > 0) WriteResult.Success else WriteResult.StaleVersion
    }

    fun observePublished(): Flow<List<ScheduleEntity>> =
        dao.observeByState(ScheduleState.PUBLISHED)

    // ---- Assignments ----

    suspend fun addAssignment(assignment: ScheduleAssignmentEntity): Long =
        dao.insertAssignment(assignment)

    suspend fun assignmentsFor(scheduleId: Long): List<ScheduleAssignmentEntity> =
        dao.findAssignmentsFor(scheduleId)

    fun observeAssignmentsFor(scheduleId: Long): Flow<List<ScheduleAssignmentEntity>> =
        dao.observeAssignmentsFor(scheduleId)

    suspend fun assignmentsForPerson(personId: Long): List<ScheduleAssignmentEntity> =
        dao.findAssignmentsForPerson(personId)

    suspend fun assignmentById(id: Long): ScheduleAssignmentEntity? =
        dao.findAssignmentById(id)

    suspend fun removeAssignment(assignment: ScheduleAssignmentEntity): Boolean =
        dao.deleteAssignment(assignment) > 0

    /**
     * Applies a validation outcome to one assignment. A second supervisor
     * editing the same row holds an older version, so their write affects
     * no rows and comes back as [WriteResult.StaleVersion].
     */
    suspend fun applyValidation(
        assignment: ScheduleAssignmentEntity,
        newState: AssignmentState,
        reason: String?
    ): WriteResult {
        val rows = dao.updateAssignmentIfCurrent(
            id = assignment.id,
            newState = newState,
            reason = reason,
            expectedVersion = assignment.version
        )
        return if (rows > 0) WriteResult.Success else WriteResult.StaleVersion
    }

    suspend fun unresolvedConflictCount(scheduleId: Long): Int =
        dao.countAssignmentsInState(scheduleId, AssignmentState.CONFLICT)

    /**
     * Publishes a schedule only when nothing is left in Conflict, which is
     * the guard the Module 4 state machine places on ApproveAndPublish.
     */
    suspend fun publish(schedule: ScheduleEntity): WriteResult {
        if (unresolvedConflictCount(schedule.id) > 0) {
            return WriteResult.Failed("Schedule still has unresolved conflicts")
        }
        val rows = dao.updateStateIfCurrent(
            id = schedule.id,
            newState = ScheduleState.PUBLISHED,
            expectedVersion = schedule.version
        )
        return if (rows > 0) WriteResult.Success else WriteResult.StaleVersion
    }

    /**
     * Commits a batch inside a single transaction. If the commit fails
     * partway, the transaction rolls back to the prior valid schedule
     * rather than leaving it half written.
     */
    suspend fun commitBatch(assignments: List<ScheduleAssignmentEntity>): WriteResult =
        try {
            dao.commitAssignments(assignments)
            WriteResult.Success
        } catch (e: Exception) {
            WriteResult.Failed(e.message ?: "Commit failed and was rolled back")
        }

    // ---- Shifts ----

    suspend fun shiftById(id: Long): ShiftEntity? = dao.findShiftById(id)

    suspend fun shiftsFor(departmentId: Long): List<ShiftEntity> =
        dao.findShiftsFor(departmentId)

    suspend fun shiftsBetween(from: LocalDate, to: LocalDate): List<ShiftEntity> =
        dao.findShiftsBetween(from, to)
}
