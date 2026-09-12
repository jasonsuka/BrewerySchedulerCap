package com.jsuka.breweryscheduler.domain.rules

import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.Severity
import com.jsuka.breweryscheduler.domain.model.ValidationResult

/**
 * Confirms the employee is free for the shift. Two checks live here because
 * both answer the same question. First, the shift must fall inside a
 * submitted availability window for that day of the week. Second, it must
 * not overlap a shift the employee already holds.
 *
 * Module 2 listed availability and overlapping shifts as separate
 * ConflictValidator concerns; they are combined in this one rule because
 * either failure means the same thing, that the employee cannot work the
 * shift.
 */
class AvailabilityRule : IValidationRule {

    override val name: String = "AvailabilityRule"

    override fun evaluate(proposal: ProposedAssignment): ValidationResult {
        val windows = proposal.availability.filter { it.dayOfWeek == proposal.shiftDayOfWeek }

        if (windows.isEmpty()) {
            return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "${proposal.personName} submitted no availability for " +
                    proposal.shiftDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            )
        }

        val covered = windows.any { window ->
            window.startMinute <= proposal.shiftStartMinute &&
                window.endMinute >= proposal.shiftEndMinute
        }

        if (!covered) {
            return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "${proposal.personName} is not available " +
                    "${formatTime(proposal.shiftStartMinute)} to ${formatTime(proposal.shiftEndMinute)}"
            )
        }

        // Existing assignments arrive sorted by start time, so the first
        // overlap found is the earliest one.
        val clash = proposal.existingAssignments
            .filter { it.date == proposal.shiftDate && it.shiftId != proposal.shiftId }
            .firstOrNull { existing ->
                existing.startMinute < proposal.shiftEndMinute &&
                    proposal.shiftStartMinute < existing.endMinute
            }

        if (clash != null) {
            return ValidationResult.Failed(
                ruleName = name,
                severity = Severity.MANDATORY,
                message = "${proposal.personName} is already assigned " +
                    "${formatTime(clash.startMinute)} to ${formatTime(clash.endMinute)} that day"
            )
        }

        return ValidationResult.Passed
    }

    private fun formatTime(minutes: Int): String =
        "%02d:%02d".format(minutes / 60, minutes % 60)
}
