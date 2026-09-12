package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jsuka.breweryscheduler.domain.model.PersonRole
import java.time.LocalDate

/**
 * Backing table for the Person hierarchy from Module 3. Employee and
 * Supervisor share one table and are distinguished by [role], which keeps
 * the schema flat while the domain layer still exposes two types.
 */
@Entity(
    tableName = "people",
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["homeDepartmentId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("homeDepartmentId")]
)
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val hireDate: LocalDate,
    val role: PersonRole,
    val homeDepartmentId: Long?
)

/** Display helper kept outside the entity so Room does not treat it as a column. */
val PersonEntity.fullName: String get() = "$firstName $lastName"
