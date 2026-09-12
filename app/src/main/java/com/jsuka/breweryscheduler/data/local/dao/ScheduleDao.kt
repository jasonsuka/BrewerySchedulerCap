package com.jsuka.breweryscheduler.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity
import com.jsuka.breweryscheduler.data.local.entity.ScheduleEntity
import com.jsuka.breweryscheduler.data.local.entity.ShiftEntity
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ScheduleDao {

    // ---- Schedules ----

    @Insert
    suspend fun insert(schedule: ScheduleEntity): Long

    @Delete
    suspend fun delete(schedule: ScheduleEntity): Int

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun findById(id: Long): ScheduleEntity?

    @Query("SELECT * FROM schedules ORDER BY weekStartDate DESC")
    suspend fun findAll(): List<ScheduleEntity>

    @Query("SELECT * FROM schedules WHERE weekStartDate = :weekStart")
    suspend fun findByWeek(weekStart: LocalDate): ScheduleEntity?

    @Query("SELECT * FROM schedules WHERE state = :state ORDER BY weekStartDate DESC")
    fun observeByState(state: ScheduleState): Flow<List<ScheduleEntity>>

    /**
     * Optimistic concurrency check from the Module 5 fault-tolerance plan.
     * The write only lands when the caller's version still matches the row,
     * so a stale edit returns 0 and the caller is told to refresh.
     */
    @Query(
        """
        UPDATE schedules
        SET state = :newState, version = version + 1
        WHERE id = :id AND version = :expectedVersion
        """
    )
    suspend fun updateStateIfCurrent(
        id: Long,
        newState: ScheduleState,
        expectedVersion: Int
    ): Int

    // ---- Assignments ----

    @Insert
    suspend fun insertAssignment(assignment: ScheduleAssignmentEntity): Long

    @Update
    suspend fun updateAssignment(assignment: ScheduleAssignmentEntity): Int

    @Delete
    suspend fun deleteAssignment(assignment: ScheduleAssignmentEntity): Int

    @Query("SELECT * FROM schedule_assignments WHERE id = :id")
    suspend fun findAssignmentById(id: Long): ScheduleAssignmentEntity?

    @Query("SELECT * FROM schedule_assignments WHERE scheduleId = :scheduleId")
    suspend fun findAssignmentsFor(scheduleId: Long): List<ScheduleAssignmentEntity>

    @Query("SELECT * FROM schedule_assignments WHERE scheduleId = :scheduleId")
    fun observeAssignmentsFor(scheduleId: Long): Flow<List<ScheduleAssignmentEntity>>

    @Query("SELECT * FROM schedule_assignments WHERE personId = :personId")
    suspend fun findAssignmentsForPerson(personId: Long): List<ScheduleAssignmentEntity>

    @Query(
        """
        SELECT COUNT(*) FROM schedule_assignments
        WHERE scheduleId = :scheduleId AND state = :state
        """
    )
    suspend fun countAssignmentsInState(scheduleId: Long, state: AssignmentState): Int

    @Query(
        """
        UPDATE schedule_assignments
        SET state = :newState, conflictReason = :reason, version = version + 1
        WHERE id = :id AND version = :expectedVersion
        """
    )
    suspend fun updateAssignmentIfCurrent(
        id: Long,
        newState: AssignmentState,
        reason: String?,
        expectedVersion: Int
    ): Int

    // ---- Shifts ----

    @Insert
    suspend fun insertShift(shift: ShiftEntity): Long

    @Query("SELECT * FROM shifts WHERE id = :id")
    suspend fun findShiftById(id: Long): ShiftEntity?

    @Query("SELECT * FROM shifts WHERE departmentId = :departmentId ORDER BY date, startMinute")
    suspend fun findShiftsFor(departmentId: Long): List<ShiftEntity>

    @Query("SELECT * FROM shifts WHERE date BETWEEN :from AND :to ORDER BY date, startMinute")
    suspend fun findShiftsBetween(from: LocalDate, to: LocalDate): List<ShiftEntity>

    /**
     * Wraps a batch commit so a failure partway through rolls the whole set
     * back to the prior valid schedule instead of leaving it half written.
     */
    @Transaction
    suspend fun commitAssignments(assignments: List<ScheduleAssignmentEntity>) {
        assignments.forEach { insertAssignment(it) }
    }
}
