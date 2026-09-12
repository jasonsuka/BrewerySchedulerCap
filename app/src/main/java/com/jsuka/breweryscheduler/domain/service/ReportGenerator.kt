package com.jsuka.breweryscheduler.domain.service

import com.jsuka.breweryscheduler.data.local.entity.fullName
import com.jsuka.breweryscheduler.data.repository.DepartmentRepository
import com.jsuka.breweryscheduler.data.repository.EmployeeRepository
import com.jsuka.breweryscheduler.data.repository.QualificationRepository
import java.time.LocalDate

data class StaffingCoverageRow(
    val departmentName: String,
    val minimumStaffing: Int,
    val staffOnRoster: Int
) {
    val meetsMinimum: Boolean get() = staffOnRoster >= minimumStaffing
}

data class QualificationGapRow(
    val departmentName: String,
    val positionTitle: String,
    val qualifiedCount: Int
) {
    /** A position only one person can fill has no cover if they are out. */
    val isSinglePointOfFailure: Boolean get() = qualifiedCount <= 1
}

data class ExpiringCredentialRow(
    val personName: String,
    val credentialName: String,
    val expirationDate: LocalDate,
    val alreadyExpired: Boolean
)

/**
 * Produces the three reports Module 3 lists as system outputs: staffing
 * coverage, qualification gaps, and upcoming certification expirations.
 */
class ReportGenerator(
    private val employees: EmployeeRepository,
    private val departments: DepartmentRepository,
    private val qualifications: QualificationRepository
) {

    suspend fun staffingCoverage(): List<StaffingCoverageRow> =
        departments.getAll().map { department ->
            StaffingCoverageRow(
                departmentName = department.name,
                minimumStaffing = department.minimumStaffing,
                staffOnRoster = employees.countInDepartment(department.id)
            )
        }

    suspend fun qualificationGaps(): List<QualificationGapRow> {
        val allCredentials = qualifications.getAll()
        return departments.getAll().flatMap { department ->
            departments.positionsFor(department.id).map { position ->
                QualificationGapRow(
                    departmentName = department.name,
                    positionTitle = position.title,
                    qualifiedCount = allCredentials.count {
                        it.code == position.requiredCredentialCode
                    }
                )
            }
        }
    }

    suspend fun expiringCertifications(
        withinDays: Long = 30,
        today: LocalDate = LocalDate.now()
    ): List<ExpiringCredentialRow> {
        val people = employees.getAll().associateBy { it.id }
        return qualifications.expiringThrough(today.plusDays(withinDays))
            .mapNotNull { credential ->
                val expires = credential.expirationDate ?: return@mapNotNull null
                ExpiringCredentialRow(
                    personName = people[credential.personId]?.fullName ?: "Unknown",
                    credentialName = credential.name,
                    expirationDate = expires,
                    alreadyExpired = expires.isBefore(today)
                )
            }
            .sortedBy { it.expirationDate }
    }
}
