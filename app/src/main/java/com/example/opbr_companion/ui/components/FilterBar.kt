package com.example.opbr_companion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.FlowRow


private val LightBackgroundColor = Color(0xFFE5E5E9)
private val DarkBackgroundColor = Color(0xFF2C2C2E)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterBar(
    selectedColor: String?,
    selectedTags: Set<String>,
    availableColors: Set<String>,
    availableTags: Set<String>,
    onColorSelected: (String?) -> Unit,
    onTagToggled: (String) -> Unit
) {

    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) LightBackgroundColor else DarkBackgroundColor

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 26.dp, vertical = 16.dp)
    ) {
        Text("Sort by:", color = LightBackgroundColor, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Text("Tags", fontWeight = FontWeight.Bold)

        Column(
            modifier = Modifier
                .height(150.dp)
                .verticalScroll(rememberScrollState())
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                availableTags.forEach { tag ->
                    OutlinedButton(
                        onClick = { onTagToggled(tag) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedTags.contains(tag)) Color(0xFFf48420) else DarkBackgroundColor.copy(0.4f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = tag,
                            fontWeight = if (selectedTags.contains(tag)) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Text("Color", fontWeight = FontWeight.Bold)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(availableColors.toList()) { color ->
                OutlinedButton(
                    onClick = {
                        onColorSelected(if (selectedColor == color) null else color)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedColor == color) Color(0xFFf48420) else DarkBackgroundColor.copy(0.4f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = color,
                        fontWeight = if (selectedColor == color) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterBarPreview() {
    val availableColors = setOf("Red", "Blue", "Green")
    val availableTags = setOf("Speed", "Power", "Technique")

    val selectedColor = remember { mutableStateOf<String?>(null) }
    val selectedTags = remember { mutableStateOf(setOf<String>()) }

    FilterBar(
        selectedColor = selectedColor.value,
        selectedTags = selectedTags.value,
        availableColors = availableColors,
        availableTags = availableTags,
        onColorSelected = { color ->
            selectedColor.value = color
        },
        onTagToggled = { tag ->
            selectedTags.value = selectedTags.value.toMutableSet().apply {
                if (contains(tag)) remove(tag) else add(tag)
            }
        }
    )
}
