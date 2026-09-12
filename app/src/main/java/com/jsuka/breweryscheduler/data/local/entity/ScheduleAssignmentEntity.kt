package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jsuka.breweryscheduler.domain.model.AssignmentState

/**
 * One employee placed in one position on one shift. [conflictReason]
 * records which rule failed so the supervisor sees the cause rather than a
 * generic error, which is the behavior the Module 6 test plan requires.
 */
@Entity(
    tableName = "schedule_assignments",
    foreignKeys = [
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = JobPositionEntity::class,
            parentColumns = ["id"],
            childColumns = ["jobPositionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ShiftEntity::class,
            parentColumns = ["id"],
            childColumns = ["shiftId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("scheduleId"),
        Index("personId"),
        Index("jobPositionId"),
        Index("shiftId")
    ]
)
data class ScheduleAssignmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scheduleId: Long,
    val personId: Long,
    val jobPositionId: Long,
    val shiftId: Long,
    val state: AssignmentState,
    val conflictReason: String? = null,
    val overridden: Boolean = false,
    val version: Int = 1
)
