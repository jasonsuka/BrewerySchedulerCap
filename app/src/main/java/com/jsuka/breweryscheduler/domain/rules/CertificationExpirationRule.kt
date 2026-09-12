package com.jsuka.breweryscheduler.domain.rules

import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.Severity
import com.jsuka.breweryscheduler.domain.model.ValidationResult

/**
 * Confirms that a certification required by the position has not lapsed as
 * of the shift date.
 *
 * The boundary is inclusive on purpose: a certification expiring on the
 * shift date is still valid for that shift, and one that expired the day
 * before is not. Those two cases are the boundary pair in the Module 6
 * test plan.
 *
 * A certification saved with no expiration date is a data defect rather
 * than a scheduling decision, so this rule throws and lets
 * ConflictValidator convert it into a conflict for the one assignment. That
 * is the exception path specified in the Module 5 fault-tolerance plan.
 */
class CertificationExpirationRule : IValidationRule {

    override val name: String = "CertificationExpirationRule"

    override fun evaluate(proposal: ProposedAssignment): ValidationResult {
        val credential = proposal.credentials[proposal.requiredCredentialCode]
            ?: return ValidationResult.Passed // QualificationRule reports this.

        if (!credential.isCertification) return ValidationResult.Passed

        val expires = credential.expirationDate
            ?: throw IllegalStateException(
                "${credential.name} for ${proposal.personName} has no expiration date on record"
            )

        if (expires.isBefore(proposal.shiftDate)) {
            return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "${credential.name} for ${proposal.personName} expired $expires"
            )
        }

        return ValidationResult.Passed
    }
}
