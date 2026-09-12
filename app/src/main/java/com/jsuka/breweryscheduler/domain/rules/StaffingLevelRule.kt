package com.jsuka.breweryscheduler.domain.rules

import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.Severity
import com.jsuka.breweryscheduler.domain.model.ValidationResult

/**
 * Confirms that pulling the employee onto this assignment does not drop
 * their home department below its minimum staffing level.
 *
 * The comparison is inclusive: leaving the home department exactly at its
 * minimum passes, and one below fails. Those two cases are the second
 * boundary pair in the Module 6 test plan.
 */
class StaffingLevelRule : IValidationRule {

    override val name: String = "StaffingLevelRule"

    override fun evaluate(proposal: ProposedAssignment): ValidationResult {
        // An employee with no home department cannot strand one.
        val home = proposal.homeDepartmentName ?: return ValidationResult.Passed

        // Staying inside their own department moves nobody.
        if (home == proposal.departmentName) return ValidationResult.Passed

        if (proposal.homeDepartmentRemainingIfAssigned < proposal.homeDepartmentMinimum) {
            return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "Moving ${proposal.personName} leaves $home with " +
                    "${proposal.homeDepartmentRemainingIfAssigned} staff against a minimum of " +
                    "${proposal.homeDepartmentMinimum}"
            )
        }

        return ValidationResult.Passed
    }
}
