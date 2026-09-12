package com.jsuka.breweryscheduler.ui.reports

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ReportsScreen(state: ReportsUiState) {
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
            Text("Reports", style = MaterialTheme.typography.titleLarge)
            Text(
                "Generated in ${state.elapsedMillis} ms",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item { SectionHeading("Staffing coverage") }
        items(state.coverage) { row ->
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(row.departmentName, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${row.staffOnRoster} of ${row.minimumStaffing} minimum",
                        color = if (row.meetsMinimum) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }
            }
        }

        item { SectionHeading("Qualification gaps") }
        items(state.gaps) { row ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (row.isSinglePointOfFailure) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(row.positionTitle, fontWeight = FontWeight.SemiBold)
                        Text(row.departmentName, style = MaterialTheme.typography.bodySmall)
                    }
                    Text("${row.qualifiedCount} qualified")
                }
            }
        }

        item { SectionHeading("Certifications expiring within 30 days") }
        if (state.expiring.isEmpty()) {
            item {
                Card { Text("Nothing expiring in the next 30 days.", Modifier.padding(14.dp)) }
            }
        } else {
            items(state.expiring) { row ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(row.personName, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${row.credentialName}, " +
                                if (row.alreadyExpired) {
                                    "expired ${row.expirationDate}"
                                } else {
                                    "expires ${row.expirationDate}"
                                },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeading(text: String) {
    Spacer(Modifier.height(4.dp))
    Text(text, style = MaterialTheme.typography.titleMedium)
}
