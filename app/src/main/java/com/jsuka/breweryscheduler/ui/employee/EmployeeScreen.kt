package com.jsuka.breweryscheduler.ui.employee

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter

private val fullDay = DateTimeFormatter.ofPattern("EEEE, MMM d")

private fun clock(minutes: Int) = "%02d:%02d".format(minutes / 60, minutes % 60)

@Composable
fun EmployeeScreen(
    state: EmployeeUiState,
    onToggleDay: (Int) -> Unit,
    onSetWindow: (Int, Int, Int) -> Unit,
    onSave: () -> Unit,
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
            Text("My schedule", style = MaterialTheme.typography.titleLarge)
            Text(
                state.personName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (state.message != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.padding(start = 14.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(state.message)
                        TextButton(onClick = onDismissMessage) { Text("Dismiss") }
                    }
                }
            }
        }

        if (state.shifts.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "No published shifts yet. Your schedule appears here once a " +
                            "supervisor publishes it.",
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            items(state.shifts) { shift ->
                Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(shift.date.format(fullDay), fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text("${shift.positionTitle}, ${shift.departmentName}")
                        Text(
                            "${clock(shift.startMinute)} to ${clock(shift.endMinute)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Text("My availability", style = MaterialTheme.typography.titleLarge)
            Text(
                "Pick the days you can work and the window you are free.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(state.availability) { day ->
            Card {
                Column(Modifier.padding(12.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(day.label, fontWeight = FontWeight.SemiBold)
                        FilterChip(
                            selected = day.selected,
                            onClick = { onToggleDay(day.dayOfWeek) },
                            label = { Text(if (day.selected) "Available" else "Off") }
                        )
                    }

                    if (day.selected) {
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = day.startMinute == 6 * 60,
                                onClick = { onSetWindow(day.dayOfWeek, 6 * 60, 14 * 60) },
                                label = { Text("Day 06:00 to 14:00") }
                            )
                            FilterChip(
                                selected = day.startMinute == 14 * 60,
                                onClick = { onSetWindow(day.dayOfWeek, 14 * 60, 22 * 60) },
                                label = { Text("Swing 14:00 to 22:00") }
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("Save availability")
            }
        }
    }
}
