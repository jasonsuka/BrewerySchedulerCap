package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A position inside a department. requiredCredentialCode is what
 * QualificationRule matches an employee's held credentials against.
 */
@Entity(
    tableName = "job_positions",
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["departmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("departmentId")]
)
data class JobPositionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val departmentId: Long,
    val title: String,
    val requiredCredentialCode: String
)
