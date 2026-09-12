package com.jsuka.breweryscheduler.domain.model

import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity
import java.time.LocalDate

/**
 * A shift an employee is already committed to, reduced to the fields the
 * overlap check needs. Held sorted by [startMinute] so AvailabilityRule can
 * detect a collision without rescanning, which is the sorted-by-start-time
 * structure the Module 3 design specifies.
 */
data class AssignedShift(
    val shiftId: Long,
    val date: LocalDate,
    val startMinute: Int,
    val endMinute: Int
)

/**
 * Everything a validation rule needs about one proposed assignment,
 * resolved up front by [com.jsuka.breweryscheduler.domain.service.SchedulingEngine].
 *
 * Rules receive this rather than a repository so each rule stays a pure
 * function of its inputs. That is what lets the Module 6 test cases run as
 * plain JVM unit tests with no database and no emulator.
 *
 * [credentials] is keyed by credential code so a qualification check is a
 * constant-time lookup rather than a repeated query, matching the dictionary
 * structure described in Module 3.
 */
data class ProposedAssignment(
    val assignmentId: Long,
    val scheduleId: Long,
    val personId: Long,
    val personName: String,
    val jobPositionId: Long,
    val positionTitle: String,
    val requiredCredentialCode: String,
    val departmentId: Long,
    val departmentName: String,
    val shiftId: Long,
    val shiftDate: LocalDate,
    val shiftStartMinute: Int,
    val shiftEndMinute: Int,
    val credentials: Map<String, CredentialEntity>,
    val availability: List<AvailabilityEntity>,
    val existingAssignments: List<AssignedShift>,
    val homeDepartmentName: String?,
    val homeDepartmentMinimum: Int,
    val homeDepartmentRemainingIfAssigned: Int,
    val evaluationDate: LocalDate = LocalDate.now()
) {
    /** Day of week of the shift, matching java.time where Monday is 1. */
    val shiftDayOfWeek: Int get() = shiftDate.dayOfWeek.value
}

/**
 * How serious a rule failure is. A mandatory failure blocks publication
 * until it is resolved or a supervisor applies an override; a warning is
 * advisory. Module 2 established this split.
 */
enum class Severity {
    MANDATORY,
    WARNING
}

/** One rule's verdict on one proposed assignment. */
sealed interface ValidationResult {

    data object Passed : ValidationResult

    data class Failed(
        val ruleName: String,
        val severity: Severity,
        val message: String
    ) : ValidationResult
}
