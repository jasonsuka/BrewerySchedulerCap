package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jsuka.breweryscheduler.domain.model.PersonRole

/**
 * Login credentials for one person. Passwords are stored only as a salted
 * hash, following the secure-development guidance cited in Module 2.
 */
@Entity(
    tableName = "user_accounts",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["username"], unique = true), Index("personId")]
)
data class UserAccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val username: String,
    val passwordSalt: String,
    val passwordHash: String,
    val role: PersonRole
)
