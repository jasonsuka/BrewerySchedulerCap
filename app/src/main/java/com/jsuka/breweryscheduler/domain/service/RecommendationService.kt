package com.jsuka.breweryscheduler.domain.service

import com.jsuka.breweryscheduler.data.local.entity.fullName
import com.jsuka.breweryscheduler.data.repository.DepartmentRepository
import com.jsuka.breweryscheduler.data.repository.EmployeeRepository
import com.jsuka.breweryscheduler.domain.model.PersonRole
import java.time.LocalDate

/**
 * One alternative employee offered for a conflicted assignment.
 * [currentWorkload] is how many shifts they already hold that week, which
 * is what the ranking is based on.
 */
data class Recommendation(
    val personId: Long,
    val personName: String,
    val currentWorkload: Int,
    val homeDepartment: String?
)

/**
 * Finds employees who could take a shift that someone else cannot.
 *
 * A candidate is offered only when every validation rule passes for them,
 * so the supervisor is never shown someone who would immediately produce
 * another conflict. Results are ranked by current workload ascending, which
 * spreads hours rather than repeatedly loading the same cross-trained
 * operator. Module 3 describes this ordering by availability and workload.
 */
class RecommendationService(
    private val employees: EmployeeRepository,
    private val departments: DepartmentRepository,
    private val contextBuilder: AssignmentContextBuilder,
    private val validator: ConflictValidator = ConflictValidator()
) {

    suspend fun alternativesFor(
        scheduleId: Long,
        jobPositionId: Long,
        shiftId: Long,
        excludePersonId: Long,
        evaluationDate: LocalDate = LocalDate.now(),
        limit: Int = 5
    ): List<Recommendation> {
        val position = departments.positionById(jobPositionId) ?: return emptyList()

        val candidates = employees.getAll().filter {
            it.role == PersonRole.EMPLOYEE && it.id != excludePersonId
        }

        val viable = mutableListOf<Recommendation>()

        for (candidate in candidates) {
            val proposal = contextBuilder.build(
                AssignmentRequest(
                    scheduleId = scheduleId,
                    personId = candidate.id,
                    jobPositionId = jobPositionId,
                    shiftId = shiftId
                ),
                evaluationDate
            ) ?: continue

            // Skip anyone who does not even hold the required credential
            // before running the full rule set, which keeps the scan cheap.
            if (!proposal.credentials.containsKey(position.requiredCredentialCode)) continue

            if (!validator.validate(proposal).isValid) continue

            viable += Recommendation(
                personId = candidate.id,
                personName = candidate.fullName,
                currentWorkload = proposal.existingAssignments.size,
                homeDepartment = proposal.homeDepartmentName
            )
        }

        return viable
            .sortedWith(compareBy({ it.currentWorkload }, { it.personName }))
            .take(limit)
    }
}
