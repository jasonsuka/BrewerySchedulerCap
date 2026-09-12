package com.jsuka.breweryscheduler.domain

import com.jsuka.breweryscheduler.domain.model.CredentialCodes
import com.jsuka.breweryscheduler.domain.model.ProposedAssignment
import com.jsuka.breweryscheduler.domain.model.Severity
import com.jsuka.breweryscheduler.domain.model.ValidationResult
import com.jsuka.breweryscheduler.domain.rules.AvailabilityRule
import com.jsuka.breweryscheduler.domain.rules.CertificationExpirationRule
import com.jsuka.breweryscheduler.domain.rules.IValidationRule
import com.jsuka.breweryscheduler.domain.rules.QualificationRule
import com.jsuka.breweryscheduler.domain.rules.StaffingLevelRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Rule-level tests. Each rule is exercised on its own so a failure points
 * at one class rather than at the validator as a whole.
 */
class ValidationRuleTest {

    private fun assertPasses(rule: IValidationRule, proposal: ProposedAssignment) {
        assertEquals(ValidationResult.Passed, rule.evaluate(proposal))
    }

    private fun assertFailsAsMandatory(rule: IValidationRule, proposal: ProposedAssignment) {
        val result = rule.evaluate(proposal)
        assertTrue("Expected ${rule.name} to fail", result is ValidationResult.Failed)
        assertEquals(Severity.MANDATORY, (result as ValidationResult.Failed).severity)
    }

    // ---- QualificationRule ----

    @Test
    fun `qualification held for the position passes`() {
        assertPasses(QualificationRule(), TestFixtures.passingProposal())
    }

    @Test
    fun `qualification earned after the shift date is not yet held`() {
        val proposal = TestFixtures.passingProposal(
            credentials = mapOf(
                CredentialCodes.FILTER to TestFixtures.qualification(
                    earned = TestFixtures.SHIFT_DATE.plusDays(3)
                )
            )
        )
        assertFailsAsMandatory(QualificationRule(), proposal)
    }

    @Test
    fun `holding a different credential does not qualify for this position`() {
        val proposal = TestFixtures.passingProposal(
            credentials = mapOf(
                CredentialCodes.PACKER to TestFixtures.qualification(code = CredentialCodes.PACKER)
            )
        )
        assertFailsAsMandatory(QualificationRule(), proposal)
    }

    // ---- CertificationExpirationRule ----

    @Test
    fun `a qualification never expires so the rule passes it through`() {
        assertPasses(CertificationExpirationRule(), TestFixtures.passingProposal())
    }

    @Test
    fun `a certification expiring in the future passes`() {
        val proposal = TestFixtures.passingProposal(
            requiredCode = CredentialCodes.FORK_TRUCK,
            credentials = mapOf(CredentialCodes.FORK_TRUCK to TestFixtures.certification())
        )
        assertPasses(CertificationExpirationRule(), proposal)
    }

    @Test(expected = IllegalStateException::class)
    fun `a certification with no expiration date throws for the validator to catch`() {
        val proposal = TestFixtures.passingProposal(
            requiredCode = CredentialCodes.FORK_TRUCK,
            credentials = mapOf(
                CredentialCodes.FORK_TRUCK to TestFixtures.certification(expires = null)
            )
        )
        CertificationExpirationRule().evaluate(proposal)
    }

    // ---- AvailabilityRule ----

    @Test
    fun `shift exactly filling the availability window passes at the boundary`() {
        val proposal = TestFixtures.passingProposal(
            availability = listOf(
                TestFixtures.availability(start = TestFixtures.DAY_START, end = TestFixtures.DAY_END)
            ),
            shiftStart = TestFixtures.DAY_START,
            shiftEnd = TestFixtures.DAY_END
        )
        assertPasses(AvailabilityRule(), proposal)
    }

    @Test
    fun `shift ending one minute past the availability window fails`() {
        val proposal = TestFixtures.passingProposal(
            availability = listOf(
                TestFixtures.availability(start = TestFixtures.DAY_START, end = TestFixtures.DAY_END)
            ),
            shiftStart = TestFixtures.DAY_START,
            shiftEnd = TestFixtures.DAY_END + 1
        )
        assertFailsAsMandatory(AvailabilityRule(), proposal)
    }

    @Test
    fun `a shift that ends exactly when another begins does not overlap`() {
        val proposal = TestFixtures.passingProposal(
            availability = listOf(TestFixtures.availability(start = 0, end = 24 * 60)),
            shiftStart = 6 * 60,
            shiftEnd = 14 * 60,
            existingAssignments = listOf(
                com.jsuka.breweryscheduler.domain.model.AssignedShift(
                    shiftId = 99L,
                    date = TestFixtures.SHIFT_DATE,
                    startMinute = 14 * 60,
                    endMinute = 22 * 60
                )
            )
        )
        assertPasses(AvailabilityRule(), proposal)
    }

    // ---- StaffingLevelRule ----

    @Test
    fun `staying inside the home department moves nobody and passes`() {
        val proposal = TestFixtures.passingProposal(
            homeDepartmentName = "Cold Side Brewing",
            departmentName = "Cold Side Brewing",
            homeDepartmentRemaining = 0
        )
        assertPasses(StaffingLevelRule(), proposal)
    }

    @Test
    fun `an employee with no home department cannot strand one`() {
        val proposal = TestFixtures.passingProposal(
            homeDepartmentName = null,
            departmentName = "Warehouse",
            homeDepartmentRemaining = 0
        )
        assertPasses(StaffingLevelRule(), proposal)
    }
}
