package com.jsuka.breweryscheduler.ui.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jsuka.breweryscheduler.AppContainer
import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class MyShift(
    val date: LocalDate,
    val positionTitle: String,
    val departmentName: String,
    val startMinute: Int,
    val endMinute: Int
)

data class AvailabilityDay(
    val dayOfWeek: Int,
    val label: String,
    val selected: Boolean,
    val startMinute: Int,
    val endMinute: Int
)

data class EmployeeUiState(
    val loading: Boolean = true,
    val personName: String = "",
    val shifts: List<MyShift> = emptyList(),
    val availability: List<AvailabilityDay> = emptyList(),
    val message: String? = null
)

/**
 * Backs the read-only employee view described in Module 3: a published
 * schedule the employee cannot edit, plus the availability they submit for
 * future schedules.
 */
class EmployeeViewModel(
    private val container: AppContainer,
    private val personId: Long
) : ViewModel() {

    private val _state = MutableStateFlow(EmployeeUiState())
    val state: StateFlow<EmployeeUiState> = _state.asStateFlow()

    private val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    init {
        refresh()
    }

    fun dismissMessage() {
        _state.value = _state.value.copy(message = null)
    }

    fun refresh() {
        viewModelScope.launch {
            val person = container.employees.getById(personId)
            val published = container.schedules.getAll()
                .filter { it.state == ScheduleState.PUBLISHED }
                .map { it.id }
                .toSet()

            // Only a published schedule is visible to an employee.
            val shifts = container.schedules.assignmentsForPerson(personId)
                .filter { it.scheduleId in published }
                .mapNotNull { assignment ->
                    val shift = container.schedules.shiftById(assignment.shiftId)
                    val position = container.departments.positionById(assignment.jobPositionId)
                    val department = position?.let { container.departments.getById(it.departmentId) }
                    if (shift == null || position == null || department == null) return@mapNotNull null
                    MyShift(
                        date = shift.date,
                        positionTitle = position.title,
                        departmentName = department.name,
                        startMinute = shift.startMinute,
                        endMinute = shift.endMinute
                    )
                }
                .sortedWith(compareBy({ it.date }, { it.startMinute }))

            val submitted = container.employees.availabilityFor(personId).associateBy { it.dayOfWeek }
            val availability = (1..7).map { day ->
                val window = submitted[day]
                AvailabilityDay(
                    dayOfWeek = day,
                    label = dayNames[day - 1],
                    selected = window != null,
                    startMinute = window?.startMinute ?: 6 * 60,
                    endMinute = window?.endMinute ?: 14 * 60
                )
            }

            _state.value = EmployeeUiState(
                loading = false,
                personName = person?.let { "${it.firstName} ${it.lastName}" } ?: "",
                shifts = shifts,
                availability = availability
            )
        }
    }

    fun toggleDay(dayOfWeek: Int) {
        val updated = _state.value.availability.map {
            if (it.dayOfWeek == dayOfWeek) it.copy(selected = !it.selected) else it
        }
        _state.value = _state.value.copy(availability = updated)
    }

    fun setWindow(dayOfWeek: Int, startMinute: Int, endMinute: Int) {
        val updated = _state.value.availability.map {
            if (it.dayOfWeek == dayOfWeek) {
                it.copy(startMinute = startMinute, endMinute = endMinute, selected = true)
            } else {
                it
            }
        }
        _state.value = _state.value.copy(availability = updated)
    }

    fun saveAvailability() {
        viewModelScope.launch {
            val windows = _state.value.availability
                .filter { it.selected }
                .map {
                    AvailabilityEntity(
                        personId = personId,
                        dayOfWeek = it.dayOfWeek,
                        startMinute = it.startMinute,
                        endMinute = it.endMinute
                    )
                }
            container.employees.replaceAvailability(personId, windows)
            _state.value = _state.value.copy(message = "Availability saved")
            refresh()
        }
    }

    class Factory(
        private val container: AppContainer,
        private val personId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            EmployeeViewModel(container, personId) as T
    }
}
