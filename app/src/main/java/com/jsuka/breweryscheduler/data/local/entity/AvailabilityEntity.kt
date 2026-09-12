package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A recurring weekly availability window submitted by an employee.
 * dayOfWeek follows java.time.DayOfWeek, so Monday is 1 and Sunday is 7.
 */
@Entity(
    tableName = "availability",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("personId")]
)
data class AvailabilityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val dayOfWeek: Int,
    val startMinute: Int,
    val endMinute: Int
)
