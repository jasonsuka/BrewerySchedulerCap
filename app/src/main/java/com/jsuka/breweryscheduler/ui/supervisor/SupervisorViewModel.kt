package com.jsuka.breweryscheduler.ui.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jsuka.breweryscheduler.AppContainer
import com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity
import com.jsuka.breweryscheduler.data.local.entity.fullName
import com.jsuka.breweryscheduler.data.repository.WriteResult
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import com.jsuka.breweryscheduler.domain.model.PersonRole
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import com.jsuka.breweryscheduler.domain.service.AssignmentOutcome
import com.jsuka.breweryscheduler.domain.service.AssignmentRequest
import com.jsuka.breweryscheduler.domain.service.Recommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

/** One position on one shift, with whoever currently fills it. */
data class SlotRow(
    val positionId: Long,
    val positionTitle: String,
    val requiredCredentialCode: String,
    val shiftId: Long,
    val assignment: ScheduleAssignmentEntity?,
    val filledByName: String?
) {
    val state: AssignmentState? get() = assignment?.state
    val isOpen: Boolean get() = assignment == null
    val isConflicted: Boolean get() = assignment?.state == AssignmentState.CONFLICT
}

data class DepartmentSection(
    val departmentId: Long,
    val departmentName: String,
    val minimumStaffing: Int,
    val slots: List<SlotRow>
)

data class CandidateRow(
    val personId: Long,
    val name: String,
    val homeDepartment: String?
)

data class SupervisorUiState(
    val loading: Boolean = true,
    val weekStart: LocalDate = LocalDate.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val scheduleId: Long = 0,
    val scheduleState: ScheduleState = ScheduleState.DRAFT,
    val sections: List<DepartmentSection> = emptyList(),
    val conflictCount: Int = 0,
    val message: String? = null,
    val pickerForSlot: SlotRow? = null,
    val candidates: List<CandidateRow> = emptyList(),
    val recommendations: List<Recommendation> = emptyList(),
    val lastConflictReason: String? = null
)

class SupervisorViewModel(private val container: AppContainer) : ViewModel() {

    private val _state = MutableStateFlow(SupervisorUiState())
    val state: StateFlow<SupervisorUiState> = _state.asStateFlow()

    init {
        val monday = LocalDate.now().with(DayOfWeek.MONDAY)
        _state.value = _state.value.copy(weekStart = monday, selectedDate = LocalDate.now())
        refresh()
    }

    fun selectDate(date: LocalDate) {
        _state.value = _state.value.copy(selectedDate = date)
        refresh()
    }

    fun dismissMessage() {
        _state.value = _state.value.copy(message = null, lastConflictReason = null)
    }

    fun refresh() {
        viewModelScope.launch {
            val current = _state.value
            val schedule = container.schedules.getOrCreateForWeek(current.weekStart)
            val assignments = container.schedules.assignmentsFor(schedule.id)
            val people = container.employees.getAll().associateBy { it.id }

            val sections = container.departments.getAll().map { department ->
                val shift = container.schedules.shiftsFor(department.id)
                    .firstOrNull { it.date == current.selectedDate }

                val slots = container.departments.positionsFor(department.id).map { position ->
                    val existing = shift?.let { s ->
                        assignments.firstOrNull {
                            it.jobPositionId == position.id && it.shiftId == s.id
                        }
                    }
                    SlotRow(
                        positionId = position.id,
                        positionTitle = position.title,
                        requiredCredentialCode = position.requiredCredentialCode,
                        shiftId = shift?.id ?: 0L,
                        assignment = existing,
                        filledByName = existing?.let { people[it.personId]?.fullName }
                    )
                }

                DepartmentSection(
                    departmentId = department.id,
                    departmentName = department.name,
                    minimumStaffing = department.minimumStaffing,
                    slots = slots
                )
            }

            _state.value = _state.value.copy(
                loading = false,
                scheduleId = schedule.id,
                scheduleState = schedule.state,
                sections = sections,
                conflictCount = assignments.count { it.state == AssignmentState.CONFLICT }
            )
        }
    }

    /** Opens the employee picker for one open slot. */
    fun beginAssign(slot: SlotRow) {
        viewModelScope.launch {
            val candidates = container.employees.getAll()
                .filter { it.role == PersonRole.EMPLOYEE }
                .map { person ->
                    CandidateRow(
                        personId = person.id,
                        name = person.fullName,
                        homeDepartment = person.homeDepartmentId?.let {
                            container.departments.getById(it)?.name
                        }
                    )
                }
                .sortedBy { it.name }

            _state.value = _state.value.copy(pickerForSlot = slot, candidates = candidates)
        }
    }

    fun cancelAssign() {
        _state.value = _state.value.copy(pickerForSlot = null, candidates = emptyList())
    }

    /**
     * Sends the proposed assignment through SchedulingEngine. A conflict is
     * kept on screen with the failing rules named and a ranked list of
     * alternatives, rather than being silently discarded.
     */
    fun assign(personId: Long) {
        val slot = _state.value.pickerForSlot ?: return
        val scheduleId = _state.value.scheduleId

        viewModelScope.launch {
            _state.value = _state.value.copy(pickerForSlot = null, candidates = emptyList())

            val outcome = container.schedulingEngine.createAssignment(
                AssignmentRequest(
                    scheduleId = scheduleId,
                    personId = personId,
                    jobPositionId = slot.positionId,
                    shiftId = slot.shiftId
                )
            )

            when (outcome) {
                is AssignmentOutcome.Valid -> {
                    _state.value = _state.value.copy(
                        message = "Assignment validated",
                        recommendations = emptyList(),
                        lastConflictReason = null
                    )
                }

                is AssignmentOutcome.Conflict -> {
                    val alternatives = container.recommendations.alternativesFor(
                        scheduleId = scheduleId,
                        jobPositionId = slot.positionId,
                        shiftId = slot.shiftId,
                        excludePersonId = personId
                    )
                    _state.value = _state.value.copy(
                        message = "Conflict: ${outcome.failedRules.joinToString(", ")}",
                        lastConflictReason = outcome.reason,
                        recommendations = alternatives
                    )
                }

                is AssignmentOutcome.Rejected ->
                    _state.value = _state.value.copy(message = outcome.reason)

                AssignmentOutcome.StaleVersion ->
                    _state.value = _state.value.copy(
                        message = "Someone else changed this assignment. Refreshing."
                    )
            }

            refresh()
        }
    }

    /** Supervisor override, permitted only to the Supervisor role. */
    fun overrideConflict(assignmentId: Long) {
        viewModelScope.launch {
            val outcome = container.schedulingEngine.overrideConflict(assignmentId)
            _state.value = _state.value.copy(
                message = when (outcome) {
                    is AssignmentOutcome.Valid -> "Conflict overridden"
                    is AssignmentOutcome.Rejected -> outcome.reason
                    AssignmentOutcome.StaleVersion -> "Someone else changed this assignment"
                    is AssignmentOutcome.Conflict -> "Assignment is still in conflict"
                },
                recommendations = emptyList(),
                lastConflictReason = null
            )
            refresh()
        }
    }

    fun removeAssignment(assignment: ScheduleAssignmentEntity) {
        viewModelScope.launch {
            container.schedules.removeAssignment(assignment)
            _state.value = _state.value.copy(
                message = "Assignment removed",
                recommendations = emptyList(),
                lastConflictReason = null
            )
            refresh()
        }
    }

    /**
     * Publication is refused while any assignment is still in conflict,
     * which is the guard the Module 4 state machine places on
     * ApproveAndPublish.
     */
    fun publish() {
        viewModelScope.launch {
            val schedule = container.schedules.getById(_state.value.scheduleId) ?: return@launch

            if (schedule.state == ScheduleState.DRAFT) {
                container.schedules.submitForReview(schedule)
            }

            val refreshed = container.schedules.getById(schedule.id) ?: return@launch

            val message = when (val result = container.schedules.publish(refreshed)) {
                is WriteResult.Success -> "Schedule published and visible to employees"
                is WriteResult.Failed -> result.reason
                is WriteResult.StaleVersion -> "Someone else changed this schedule. Refreshing."
            }

            _state.value = _state.value.copy(message = message)
            refresh()
        }
    }

    class Factory(private val container: AppContainer) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SupervisorViewModel(container) as T
    }
}
