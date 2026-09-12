package com.jsuka.breweryscheduler.domain

import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity
import com.jsuka.breweryscheduler.domain.model.AssignedShift
import com.jsuka.breweryscheduler.domain.model.CredentialCodes
import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import java.time.LocalDate

/**
 * Builders for the validation tests. Every value has a passing default, so
 * each test changes only the one field it is actually about. That keeps the
 * boundary cases from the Module 6 test plan readable.
 */
object TestFixtures {

    val SHIFT_DATE: LocalDate = LocalDate.of(2026, 9, 14) // A Monday
    const val DAY_START = 6 * 60
    const val DAY_END = 14 * 60

    fun qualification(
        personId: Long = 1L,
        code: String = CredentialCodes.FILTER,
        earned: LocalDate = SHIFT_DATE.minusYears(1)
    ) = CredentialEntity(
        id = 0,
        personId = personId,
        code = code,
        name = "$code Qualification",
        issuingAuthority = "Internal Training",
        earnedDate = earned,
        isCertification = false,
        expirationDate = null,
        renewalRequired = false
    )

    fun certification(
        personId: Long = 1L,
        code: String = CredentialCodes.FORK_TRUCK,
        earned: LocalDate = SHIFT_DATE.minusYears(1),
        expires: LocalDate? = SHIFT_DATE.plusMonths(6)
    ) = CredentialEntity(
        id = 0,
        personId = personId,
        code = code,
        name = "Fork Truck Certification",
        issuingAuthority = "State Occupational Safety Board",
        earnedDate = earned,
        isCertification = true,
        expirationDate = expires,
        renewalRequired = true
    )

    fun availability(
        personId: Long = 1L,
        dayOfWeek: Int = SHIFT_DATE.dayOfWeek.value,
        start: Int = DAY_START,
        end: Int = DAY_END
    ) = AvailabilityEntity(
        id = 0,
        personId = personId,
        dayOfWeek = dayOfWeek,
        startMinute = start,
        endMinute = end
    )

    /**
     * A proposal that passes every rule. Tests override one field at a time
     * to isolate the rule under test.
     */
    fun passingProposal(
        credentials: Map<String, CredentialEntity> = mapOf(
            CredentialCodes.FILTER to qualification()
        ),
        requiredCode: String = CredentialCodes.FILTER,
        availability: List<AvailabilityEntity> = listOf(availability()),
        existingAssignments: List<AssignedShift> = emptyList(),
        homeDepartmentName: String? = "Cold Side Brewing",
        departmentName: String = "Cold Side Brewing",
        homeDepartmentMinimum: Int = 2,
        homeDepartmentRemaining: Int = 2,
        shiftStart: Int = DAY_START,
        shiftEnd: Int = DAY_END,
        shiftDate: LocalDate = SHIFT_DATE
    ) = ProposedAssignment(
        assignmentId = 100L,
        scheduleId = 1L,
        personId = 1L,
        personName = "Nina Alvarez",
        jobPositionId = 7L,
        positionTitle = "Filter Operator",
        requiredCredentialCode = requiredCode,
        departmentId = 4L,
        departmentName = departmentName,
        shiftId = 50L,
        shiftDate = shiftDate,
        shiftStartMinute = shiftStart,
        shiftEndMinute = shiftEnd,
        credentials = credentials,
        availability = availability,
        existingAssignments = existingAssignments,
        homeDepartmentName = homeDepartmentName,
        homeDepartmentMinimum = homeDepartmentMinimum,
        homeDepartmentRemainingIfAssigned = homeDepartmentRemaining,
        evaluationDate = shiftDate
    )
}
