package com.jsuka.breweryscheduler.ui.supervisor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import java.time.format.DateTimeFormatter

private val dayLabel = DateTimeFormatter.ofPattern("EEE d")

@Composable
fun SupervisorScreen(
    state: SupervisorUiState,
    onSelectDate: (java.time.LocalDate) -> Unit,
    onBeginAssign: (SlotRow) -> Unit,
    onOverride: (Long) -> Unit,
    onRemove: (com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity) -> Unit,
    onPublish: () -> Unit,
    onAssign: (Long) -> Unit,
    onCancelAssign: () -> Unit,
    onDismissMessage: () -> Unit
) {
    if (state.loading) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { CircularProgressIndicator() }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items((0..4).toList()) { offset ->
                    val date = state.weekStart.plusDays(offset.toLong())
                    FilterChip(
                        selected = date == state.selectedDate,
                        onClick = { onSelectDate(date) },
                        label = { Text(date.format(dayLabel)) }
                    )
                }
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Week of ${state.weekStart}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        statusLine(state),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (state.conflictCount > 0) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
                Button(
                    onClick = onPublish,
                    enabled = state.scheduleState != ScheduleState.PUBLISHED
                ) {
                    Text(if (state.scheduleState == ScheduleState.PUBLISHED) "Published" else "Publish")
                }
            }
        }

        if (state.message != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.lastConflictReason != null) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(state.message, fontWeight = FontWeight.SemiBold)
                        if (state.lastConflictReason != null) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                state.lastConflictReason,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (state.recommendations.isNotEmpty()) {
                            Spacer(Modifier.height(10.dp))
                            Text("Suggested alternatives", style = MaterialTheme.typography.titleSmall)
                            Spacer(Modifier.height(4.dp))
                            state.recommendations.forEach { rec ->
                                Text(
                                    "${rec.personName}, ${rec.currentWorkload} shifts assigned" +
                                        (rec.homeDepartment?.let { ", $it" } ?: ""),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        TextButton(onClick = onDismissMessage) { Text("Dismiss") }
                    }
                }
            }
        }

        items(state.sections) { section ->
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(section.departmentName, fontWeight = FontWeight.SemiBold)
                        Text(
                            "minimum ${section.minimumStaffing}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()

                    section.slots.forEach { slot ->
                        SlotRowView(
                            slot = slot,
                            onBeginAssign = { onBeginAssign(slot) },
                            onOverride = onOverride,
                            onRemove = onRemove
                        )
                    }
                }
            }
        }
    }

    if (state.pickerForSlot != null) {
        AlertDialog(
            onDismissRequest = onCancelAssign,
            title = { Text("Assign ${state.pickerForSlot.positionTitle}") },
            text = {
                LazyColumn {
                    items(state.candidates) { candidate ->
                        ListItem(
                            headlineContent = { Text(candidate.name) },
                            supportingContent = {
                                Text(candidate.homeDepartment ?: "No home department")
                            },
                            modifier = Modifier.clickable { onAssign(candidate.personId) }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = onCancelAssign) { Text("Cancel") } }
        )
    }
}

@Composable
private fun SlotRowView(
    slot: SlotRow,
    onBeginAssign: () -> Unit,
    onOverride: (Long) -> Unit,
    onRemove: (com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity) -> Unit
) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.padding(end = 8.dp)) {
                Text(slot.positionTitle, style = MaterialTheme.typography.bodyMedium)
                Text(
                    slot.filledByName ?: "Open",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (slot.isOpen) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            when {
                slot.shiftId == 0L ->
                    Text("No shift", style = MaterialTheme.typography.bodySmall)

                slot.isOpen ->
                    OutlinedButton(onClick = onBeginAssign) { Text("Assign") }

                else ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            onClick = { },
                            enabled = false,
                            label = { Text(stateLabel(slot.state)) }
                        )
                        TextButton(onClick = { slot.assignment?.let(onRemove) }) {
                            Text("Remove")
                        }
                    }
            }
        }

        if (slot.isConflicted) {
            slot.assignment?.conflictReason?.let { reason ->
                Text(
                    reason,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            slot.assignment?.let { assignment ->
                TextButton(onClick = { onOverride(assignment.id) }) {
                    Text("Override as supervisor")
                }
            }
        }
    }
}

private fun stateLabel(state: AssignmentState?): String = when (state) {
    AssignmentState.VALID -> "Valid"
    AssignmentState.CONFLICT -> "Conflict"
    AssignmentState.PUBLISHED -> "Published"
    AssignmentState.PENDING_VALIDATION -> "Checking"
    AssignmentState.DRAFT -> "Draft"
    null -> "Open"
}

private fun statusLine(state: SupervisorUiState): String {
    val stateText = when (state.scheduleState) {
        ScheduleState.DRAFT -> "Draft"
        ScheduleState.UNDER_REVIEW -> "Under review"
        ScheduleState.PUBLISHED -> "Published"
        ScheduleState.ARCHIVED -> "Archived"
    }
    return if (state.conflictCount > 0) {
        "$stateText, ${state.conflictCount} unresolved conflict" +
            if (state.conflictCount == 1) "" else "s"
    } else {
        "$stateText, no conflicts"
    }
}
