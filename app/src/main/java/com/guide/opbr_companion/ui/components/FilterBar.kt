package com.guide.opbr_companion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp


private val LightBackgroundColor = Color(0xFFE5E5E9)
private val DarkBackgroundColor = Color(0xFF2C2C2E)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterBar(
    labelText: String,
    selectedColor: String? = null,
    selectedTags: Set<String>,
    availableColors: Set<String> = emptySet(),
    availableTags: Set<String>,
    onColorSelected: ((String?) -> Unit)? = null,
    onTagToggled: (String) -> Unit,
    tagSectionHeight: Dp? = null,
    centerTags: Boolean = false
) {
    val tagSectionModifier = Modifier
        .then(if (tagSectionHeight != null) Modifier.height(tagSectionHeight) else Modifier)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 16.dp)
    ) {
        Text(
            "Sort by:",
            color = LightBackgroundColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Text(labelText, fontWeight = FontWeight.Bold, color = Color(0xFF007FFF))

        Column(tagSectionModifier) {
            FlowRow(
                horizontalArrangement = if (centerTags) Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ) else Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                availableTags.forEach { tag ->
                    Button(
                        onClick = { onTagToggled(tag) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedTags.contains(tag)) Color(0xFFf48420) else DarkBackgroundColor.copy(
                                0.4f
                            ),
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

        if (availableColors.isNotEmpty() && onColorSelected != null) {
            Text("Color", fontWeight = FontWeight.Bold)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(availableColors.toList()) { color ->
                    Button(
                        onClick = {
                            onColorSelected(if (selectedColor == color) null else color)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedColor == color) Color(0xFFf48420) else DarkBackgroundColor.copy(
                                0.4f
                            ),
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
}

@Preview(showBackground = true)
@Composable
fun FilterBarPreview() {
    val availableColors = setOf("Red", "Blue", "Green")
    val availableTags = setOf("Speed", "Power", "Technique")

    val selectedColor = remember { mutableStateOf<String?>(null) }
    val selectedTags = remember { mutableStateOf(setOf<String>()) }

    FilterBar(
        labelText = "Tags",
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
