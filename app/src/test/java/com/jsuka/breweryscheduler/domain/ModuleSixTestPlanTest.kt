package com.jsuka.breweryscheduler.domain

import com.jsuka.breweryscheduler.domain.model.CredentialCodes
import com.jsuka.breweryscheduler.domain.service.ConflictValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * These tests correspond directly to the rows of Table 1 in the Module 6
 * system test plan. Each test name states the input and the expected
 * result so a failure points straight back at the row it came from.
 */
class ModuleSixTestPlanTest {

    private val validator = ConflictValidator()

    // ---- UC2: a fully valid assignment ----

    @Test
    fun `qualified and available employee on a matching shift is valid`() {
        val report = validator.validate(TestFixtures.passingProposal())
        assertTrue(report.failures.toString(), report.isValid)
        assertTrue(report.failures.isEmpty())
    }

    // ---- UC3: certification expiration boundary pair ----

    @Test
    fun `certification expiring the day before the shift fails`() {
        val proposal = TestFixtures.passingProposal(
            requiredCode = CredentialCodes.FORK_TRUCK,
            credentials = mapOf(
                CredentialCodes.FORK_TRUCK to TestFixtures.certification(
                    expires = TestFixtures.SHIFT_DATE.minusDays(1)
                )
            )
        )

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("CertificationExpirationRule"))
    }

    @Test
    fun `certification expiring on the shift date passes at the boundary`() {
        val proposal = TestFixtures.passingProposal(
            requiredCode = CredentialCodes.FORK_TRUCK,
            credentials = mapOf(
                CredentialCodes.FORK_TRUCK to TestFixtures.certification(
                    expires = TestFixtures.SHIFT_DATE
                )
            )
        )

        val report = validator.validate(proposal)

        assertTrue(report.failures.toString(), report.isValid)
    }

    // ---- UC2: staffing level boundary pair ----

    @Test
    fun `reassignment leaving the home department at its minimum passes`() {
        val proposal = TestFixtures.passingProposal(
            homeDepartmentName = "Cold Side Brewing",
            departmentName = "Production",
            requiredCode = CredentialCodes.FILLER,
            credentials = mapOf(
                CredentialCodes.FILLER to TestFixtures.qualification(code = CredentialCodes.FILLER)
            ),
            homeDepartmentMinimum = 2,
            homeDepartmentRemaining = 2
        )

        val report = validator.validate(proposal)

        assertTrue(report.failures.toString(), report.isValid)
    }

    @Test
    fun `reassignment leaving the home department one below its minimum fails`() {
        val proposal = TestFixtures.passingProposal(
            homeDepartmentName = "Cold Side Brewing",
            departmentName = "Production",
            requiredCode = CredentialCodes.FILLER,
            credentials = mapOf(
                CredentialCodes.FILLER to TestFixtures.qualification(code = CredentialCodes.FILLER)
            ),
            homeDepartmentMinimum = 2,
            homeDepartmentRemaining = 1
        )

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("StaffingLevelRule"))
        assertTrue(report.summary()!!.contains("minimum of 2"))
    }

    // ---- Failure inputs from the Module 6 plan ----

    @Test
    fun `employee holding no qualification for the position fails`() {
        val proposal = TestFixtures.passingProposal(credentials = emptyMap())

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("QualificationRule"))
    }

    @Test
    fun `certification with no expiration date becomes a conflict rather than a crash`() {
        val proposal = TestFixtures.passingProposal(
            requiredCode = CredentialCodes.FORK_TRUCK,
            credentials = mapOf(
                CredentialCodes.FORK_TRUCK to TestFixtures.certification(expires = null)
            )
        )

        // The rule throws; ConflictValidator must catch it and carry on.
        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("CertificationExpirationRule"))
        assertTrue(report.summary()!!.contains("no expiration date"))
    }

    @Test
    fun `employee unavailable for the shift window fails`() {
        val proposal = TestFixtures.passingProposal(
            availability = listOf(
                TestFixtures.availability(start = 14 * 60, end = 22 * 60) // swing only
            )
        )

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("AvailabilityRule"))
    }

    @Test
    fun `employee with no availability submitted for that day fails`() {
        val proposal = TestFixtures.passingProposal(
            availability = listOf(TestFixtures.availability(dayOfWeek = 6)) // Saturday only
        )

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("AvailabilityRule"))
    }

    @Test
    fun `employee already assigned to an overlapping shift fails`() {
        val proposal = TestFixtures.passingProposal(
            existingAssignments = listOf(
                com.jsuka.breweryscheduler.domain.model.AssignedShift(
                    shiftId = 99L,
                    date = TestFixtures.SHIFT_DATE,
                    startMinute = 10 * 60,
                    endMinute = 18 * 60
                )
            )
        )

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertTrue(report.failedRuleNames.contains("AvailabilityRule"))
        assertTrue(report.summary()!!.contains("already assigned"))
    }

    @Test
    fun `an assignment on a different day does not count as an overlap`() {
        val proposal = TestFixtures.passingProposal(
            existingAssignments = listOf(
                com.jsuka.breweryscheduler.domain.model.AssignedShift(
                    shiftId = 99L,
                    date = TestFixtures.SHIFT_DATE.plusDays(1),
                    startMinute = 6 * 60,
                    endMinute = 14 * 60
                )
            )
        )

        assertTrue(validator.validate(proposal).isValid)
    }

    // ---- Every failing rule must be named, not just the first ----

    @Test
    fun `an employee failing several rules has all of them reported`() {
        val proposal = TestFixtures.passingProposal(
            credentials = emptyMap(),
            availability = emptyList(),
            homeDepartmentName = "Cold Side Brewing",
            departmentName = "Warehouse",
            homeDepartmentMinimum = 2,
            homeDepartmentRemaining = 0
        )

        val report = validator.validate(proposal)

        assertFalse(report.isValid)
        assertEquals(3, report.failures.size)
        assertTrue(report.failedRuleNames.contains("QualificationRule"))
        assertTrue(report.failedRuleNames.contains("AvailabilityRule"))
        assertTrue(report.failedRuleNames.contains("StaffingLevelRule"))
    }
}
