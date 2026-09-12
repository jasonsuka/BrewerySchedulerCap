package com.jsuka.breweryscheduler.domain.service

import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.Severity
import com.jsuka.breweryscheduler.domain.model.ValidationResult
import com.jsuka.breweryscheduler.domain.rules.AvailabilityRule
import com.jsuka.breweryscheduler.domain.rules.CertificationExpirationRule
import com.jsuka.breweryscheduler.domain.rules.IValidationRule
import com.jsuka.breweryscheduler.domain.rules.QualificationRule
import com.jsuka.breweryscheduler.domain.rules.StaffingLevelRule

/**
 * Combined verdict across every rule. Failures are collected rather than
 * short-circuited so the supervisor is told every reason an assignment was
 * rejected. Module 6 requires naming the specific rules that failed instead
 * of returning one generic error, because a supervisor who cannot see the
 * cause of a conflict cannot resolve it.
 */
data class ValidationReport(
    val failures: List<ValidationResult.Failed>
) {
    val isValid: Boolean get() = failures.none { it.severity == Severity.MANDATORY }
    val hasWarnings: Boolean get() = failures.any { it.severity == Severity.WARNING }
    val failedRuleNames: List<String> get() = failures.map { it.ruleName }

    /** One-line summary stored on the assignment as its conflict reason. */
    fun summary(): String? =
        if (failures.isEmpty()) null else failures.joinToString("; ") { it.message }
}

/**
 * Runs every validation rule against a proposed assignment.
 *
 * Each rule's evaluate call is wrapped in its own try/catch, which is the
 * exception-handling technique specified in the Module 5 fault-tolerance
 * plan. A rule that throws, such as CertificationExpirationRule meeting a
 * certification with no expiration date, becomes a conflict for that one
 * assignment while the remaining rules still run and the surrounding batch
 * keeps processing.
 */
class ConflictValidator(
    private val rules: List<IValidationRule> = defaultRules()
) {

    fun validate(proposal: ProposedAssignment): ValidationReport {
        val failures = mutableListOf<ValidationResult.Failed>()

        for (rule in rules) {
            val result = try {
                rule.evaluate(proposal)
            } catch (e: Exception) {
                ValidationResult.Failed(
                    ruleName = rule.name,
                    severity = Severity.MANDATORY,
                    message = e.message ?: "${rule.name} could not be evaluated"
                )
            }

            if (result is ValidationResult.Failed) failures += result
        }

        return ValidationReport(failures)
    }

    companion object {
        fun defaultRules(): List<IValidationRule> = listOf(
            QualificationRule(),
            CertificationExpirationRule(),
            AvailabilityRule(),
            StaffingLevelRule()
        )
    }
}
