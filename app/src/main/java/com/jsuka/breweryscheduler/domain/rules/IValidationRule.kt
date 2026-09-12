package com.jsuka.breweryscheduler.domain.rules

import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.ValidationResult

/**
 * The single-method validation contract from the Module 3 design. Each rule
 * evaluates a proposed assignment and reports a verdict, which lets
 * ConflictValidator run every rule without knowing how any of them works.
 */
interface IValidationRule {

    /** Short identifier used in conflict messages and test assertions. */
    val name: String

    fun evaluate(proposal: ProposedAssignment): ValidationResult
}
