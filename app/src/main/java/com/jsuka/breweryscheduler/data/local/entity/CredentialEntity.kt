package com.jsuka.breweryscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A credential held by one person. Mirrors the Module 3 Credential base
 * class: name, issuing authority and earned date are shared, while
 * expirationDate and renewalRequired apply only when isCertification is
 * true. A qualification leaves expirationDate null.
 */
@Entity(
    tableName = "credentials",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("personId"), Index("code")]
)
data class CredentialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val code: String,
    val name: String,
    val issuingAuthority: String,
    val earnedDate: LocalDate,
    val isCertification: Boolean,
    val expirationDate: LocalDate?,
    val renewalRequired: Boolean
)
