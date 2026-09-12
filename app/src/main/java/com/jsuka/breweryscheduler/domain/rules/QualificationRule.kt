package com.jsuka.breweryscheduler.domain.rules

import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.Severity
import com.jsuka.breweryscheduler.domain.model.ValidationResult

/**
 * Confirms the employee holds the credential the position requires.
 * A missing qualification is a mandatory failure: an untrained operator
 * cannot be scheduled onto the position regardless of availability.
 */
class QualificationRule : IValidationRule {

    override val name: String = "QualificationRule"

    override fun evaluate(proposal: ProposedAssignment): ValidationResult {
        val held = proposal.credentials[proposal.requiredCredentialCode]
            ?: return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "${proposal.personName} is not qualified for ${proposal.positionTitle}"
            )

        // A credential earned after the shift date has not been held yet.
        if (held.earnedDate.isAfter(proposal.shiftDate)) {
            return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "${proposal.personName} does not hold ${held.name} until ${held.earnedDate}"
            )
        }

        return ValidationResult.Passed
    }
}
