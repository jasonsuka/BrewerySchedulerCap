package com.jsuka.breweryscheduler.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jsuka.breweryscheduler.AppContainer
import com.jsuka.breweryscheduler.domain.model.SessionState
import com.jsuka.breweryscheduler.ui.auth.LoginScreen
import com.jsuka.breweryscheduler.ui.auth.SessionViewModel
import com.jsuka.breweryscheduler.ui.employee.EmployeeScreen
import com.jsuka.breweryscheduler.ui.employee.EmployeeViewModel
import com.jsuka.breweryscheduler.ui.reports.ReportsScreen
import com.jsuka.breweryscheduler.ui.reports.ReportsViewModel
import com.jsuka.breweryscheduler.ui.supervisor.SupervisorScreen
import com.jsuka.breweryscheduler.ui.supervisor.SupervisorViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private enum class Tab(val label: String, val icon: ImageVector) {
    SCHEDULE("Schedule", Icons.Filled.DateRange),
    REPORTS("Reports", Icons.Filled.Info)
}

/**
 * Routes between the supervisor and employee experiences based on the
 * authenticated role, which is the role-based access described in Module 2.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(container: AppContainer) {
    val sessionViewModel: SessionViewModel = viewModel(
        factory = SessionViewModel.Factory(container)
    )
    val session by sessionViewModel.state.collectAsState()

    when (session.state) {
        SessionState.LOGGED_OUT, SessionState.AUTHENTICATING -> {
            LoginScreen(
                state = session,
                onSignIn = sessionViewModel::signIn
            )
            return
        }

        SessionState.SESSION_EXPIRED -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Session expired") },
                text = {
                    Text(
                        "You were signed out after a period of inactivity. " +
                            "Nothing you had in progress was published."
                    )
                },
                confirmButton = {
                    TextButton(onClick = sessionViewModel::acknowledgeExpiration) {
                        Text("Sign in again")
                    }
                }
            )
            return
        }

        SessionState.AUTHENTICATED -> Unit
    }

    var tab by remember { mutableStateOf(Tab.SCHEDULE) }
    val person = session.person

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Brewery Scheduler")
                        Text(
                            "${person?.firstName ?: ""} ${person?.lastName ?: ""}, " +
                                if (session.isSupervisor) "Supervisor" else "Employee",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    TextButton(onClick = sessionViewModel::signOut) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = null)
                        Text("Sign out")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (session.isSupervisor) {
                NavigationBar {
                    Tab.entries.forEach { entry ->
                        NavigationBarItem(
                            selected = tab == entry,
                            onClick = {
                                sessionViewModel.recordActivity()
                                tab = entry
                            },
                            icon = { Icon(entry.icon, contentDescription = null) },
                            label = { Text(entry.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (session.isSupervisor) {
                when (tab) {
                    Tab.SCHEDULE -> SupervisorRoute(container)
                    Tab.REPORTS -> ReportsRoute(container)
                }
            } else {
                EmployeeRoute(container, person?.id ?: 0L)
            }
        }
    }
}

@Composable
private fun SupervisorRoute(container: AppContainer) {
    val vm: SupervisorViewModel = viewModel(factory = SupervisorViewModel.Factory(container))
    val state by vm.state.collectAsState()

    SupervisorScreen(
        state = state,
        onSelectDate = vm::selectDate,
        onBeginAssign = vm::beginAssign,
        onOverride = vm::overrideConflict,
        onRemove = vm::removeAssignment,
        onPublish = vm::publish,
        onAssign = vm::assign,
        onCancelAssign = vm::cancelAssign,
        onDismissMessage = vm::dismissMessage
    )
}

@Composable
private fun ReportsRoute(container: AppContainer) {
    val vm: ReportsViewModel = viewModel(factory = ReportsViewModel.Factory(container))
    val state by vm.state.collectAsState()
    ReportsScreen(state = state)
}

@Composable
private fun EmployeeRoute(container: AppContainer, personId: Long) {
    val vm: EmployeeViewModel = viewModel(
        factory = EmployeeViewModel.Factory(container, personId)
    )
    val state by vm.state.collectAsState()

    EmployeeScreen(
        state = state,
        onToggleDay = vm::toggleDay,
        onSetWindow = vm::setWindow,
        onSave = vm::saveAvailability,
        onDismissMessage = vm::dismissMessage
    )
}
