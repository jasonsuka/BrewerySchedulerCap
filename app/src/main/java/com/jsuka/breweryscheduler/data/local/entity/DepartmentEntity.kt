package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One of the four brewery departments. minimumStaffing is the floor
 * StaffingLevelRule enforces before an assignment may be published.
 */
@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val minimumStaffing: Int
)
