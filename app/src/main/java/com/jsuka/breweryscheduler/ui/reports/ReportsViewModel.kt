package com.jsuka.breweryscheduler.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jsuka.breweryscheduler.AppContainer
import com.jsuka.breweryscheduler.domain.service.ExpiringCredentialRow
import com.jsuka.breweryscheduler.domain.service.QualificationGapRow
import com.jsuka.breweryscheduler.domain.service.StaffingCoverageRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReportsUiState(
    val loading: Boolean = true,
    val coverage: List<StaffingCoverageRow> = emptyList(),
    val gaps: List<QualificationGapRow> = emptyList(),
    val expiring: List<ExpiringCredentialRow> = emptyList(),
    val elapsedMillis: Long = 0
)

class ReportsViewModel(private val container: AppContainer) : ViewModel() {

    private val _state = MutableStateFlow(ReportsUiState())
    val state: StateFlow<ReportsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            // Timed so the run can be checked against the Module 6
            // performance target for report generation.
            val started = System.currentTimeMillis()
            val coverage = container.reports.staffingCoverage()
            val gaps = container.reports.qualificationGaps()
            val expiring = container.reports.expiringCertifications()
            val elapsed = System.currentTimeMillis() - started

            _state.value = ReportsUiState(
                loading = false,
                coverage = coverage,
                gaps = gaps,
                expiring = expiring,
                elapsedMillis = elapsed
            )
        }
    }

    class Factory(private val container: AppContainer) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ReportsViewModel(container) as T
    }
}
