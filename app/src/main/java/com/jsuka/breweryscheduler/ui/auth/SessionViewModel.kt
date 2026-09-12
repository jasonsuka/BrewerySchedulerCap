package com.jsuka.breweryscheduler.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jsuka.breweryscheduler.AppContainer
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity
import com.jsuka.breweryscheduler.domain.model.PersonRole
import com.jsuka.breweryscheduler.domain.model.SessionState
import com.jsuka.breweryscheduler.domain.service.AuthOutcome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionUiState(
    val state: SessionState = SessionState.LOGGED_OUT,
    val person: PersonEntity? = null,
    val role: PersonRole? = null,
    val errorMessage: String? = null
) {
    val isSupervisor: Boolean get() = role == PersonRole.SUPERVISOR
}

/**
 * Owns the UserAccount state machine from Module 4: LoggedOut,
 * Authenticating, Authenticated and SessionExpired.
 *
 * A session expires after [TIMEOUT_MILLIS] of inactivity. Expiry does not
 * discard unsaved work; it only blocks further action until the user signs
 * in again, which is the behavior the Module 6 test plan requires.
 */
class SessionViewModel(private val container: AppContainer) : ViewModel() {

    private val _state = MutableStateFlow(SessionUiState())
    val state: StateFlow<SessionUiState> = _state.asStateFlow()

    private var lastActivityAt: Long = System.currentTimeMillis()

    fun signIn(username: String, password: String) {
        _state.value = _state.value.copy(
            state = SessionState.AUTHENTICATING,
            errorMessage = null
        )

        viewModelScope.launch {
            when (val outcome = container.auth.authenticate(username, password)) {
                is AuthOutcome.Success -> {
                    lastActivityAt = System.currentTimeMillis()
                    _state.value = SessionUiState(
                        state = SessionState.AUTHENTICATED,
                        person = outcome.person,
                        role = outcome.role
                    )
                }

                AuthOutcome.InvalidCredentials -> {
                    _state.value = SessionUiState(
                        state = SessionState.LOGGED_OUT,
                        errorMessage = "That username and password combination was not recognized"
                    )
                }
            }
        }
    }

    fun signOut() {
        _state.value = SessionUiState(state = SessionState.LOGGED_OUT)
    }

    /** Called on each navigation so an active user is not timed out. */
    fun recordActivity() {
        val current = _state.value
        if (current.state != SessionState.AUTHENTICATED) return

        if (System.currentTimeMillis() - lastActivityAt > TIMEOUT_MILLIS) {
            _state.value = current.copy(state = SessionState.SESSION_EXPIRED)
        } else {
            lastActivityAt = System.currentTimeMillis()
        }
    }

    /** AcknowledgeExpiration in the Module 4 state machine. */
    fun acknowledgeExpiration() {
        _state.value = SessionUiState(state = SessionState.LOGGED_OUT)
    }

    class Factory(private val container: AppContainer) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SessionViewModel(container) as T
    }

    companion object {
        const val TIMEOUT_MILLIS = 15 * 60 * 1000L
    }
}
