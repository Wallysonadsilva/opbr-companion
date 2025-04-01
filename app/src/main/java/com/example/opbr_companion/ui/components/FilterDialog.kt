package com.example.opbr_companion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.opbr_companion.model.FilterState

@Composable
fun FilterDialog(
    filterState: FilterState,
    availableColors: Set<String>,
    availableTags: Set<String>,
    onApply: (String?, Set<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(filterState.selectedColor) }
    val selectedTags = remember { mutableStateListOf<String>().apply { addAll(filterState.selectedTags) } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Color", style = MaterialTheme.typography.labelLarge)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(availableColors.toList()) { color ->
                        Text(
                            text = color,
                            fontWeight = if (selectedColor == color) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .clickable {
                                    selectedColor = if (selectedColor == color) null else color
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Tags", style = MaterialTheme.typography.labelLarge)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(availableTags.toList()) { tag ->
                        Text(
                            text = tag,
                            fontWeight = if (selectedTags.contains(tag)) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .clickable {
                                    if (selectedTags.contains(tag)) {
                                        selectedTags.remove(tag)
                                    } else {
                                        selectedTags.add(tag)
                                    }
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onApply(selectedColor, selectedTags.toSet())
                onDismiss()
            }) {
                Text("Apply", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Black)
            }
        }
    )
}
