package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import java.time.LocalDate

/**
 * A weekly schedule. [version] backs the optimistic concurrency check
 * described in Module 5: an update carrying a stale version is rejected
 * rather than silently overwriting a newer one.
 */
@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weekStartDate: LocalDate,
    val state: ScheduleState,
    val version: Int = 1
)
