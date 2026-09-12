package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A work period in one department on one date. Times are minutes past
 * midnight so the validator can sort and compare ranges cheaply, which is
 * the sorted-by-start-time structure the Module 3 design calls for.
 */
@Entity(
    tableName = "shifts",
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["departmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("departmentId"), Index("date")]
)
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val departmentId: Long,
    val date: LocalDate,
    val startMinute: Int,
    val endMinute: Int,
    val label: String
)
