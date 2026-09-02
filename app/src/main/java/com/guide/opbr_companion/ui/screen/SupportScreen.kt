package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.guide.opbr_companion.R
import com.guide.opbr_companion.data.model.Support
import com.guide.opbr_companion.ui.components.FilterBar
import com.guide.opbr_companion.ui.components.SupportListSkeleton
import com.guide.opbr_companion.ui.theme.OpbrcompanionTheme
import com.guide.opbr_companion.ui.viewmodel.SupportViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    viewModel: SupportViewModel = viewModel(),
    showFilterBar: Boolean,
    onSupportClick: (Int) -> Unit = {}
) {
    val supports by viewModel.filteredSupportList.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val allTags = viewModel.allTags
    val allColors = viewModel.allColors
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (showFilterBar) {
            FilterBar(
                labelText = "Tags",
                selectedColor = filterState.selectedColor,
                selectedTags = filterState.selectedTags,
                availableColors = allColors.toSet(),
                availableTags = allTags.toSet(),
                onColorSelected = { color ->
                    viewModel.updateFilter(
                        color,
                        filterState.selectedTags
                    )
                },
                onTagToggled = { tag ->
                    val updatedTags = filterState.selectedTags.toMutableSet()
                    if (updatedTags.contains(tag)) updatedTags.remove(tag) else updatedTags.add(tag)
                    viewModel.updateFilter(filterState.selectedColor, updatedTags)
                },
                tagSectionHeight = 150.dp
            )
        }

        if (isLoading) {
            SupportListSkeleton()
        } else if (supports.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "No support data found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn {
                items(supports.size) { index ->
                    val support = supports[index]
                    SupportItem(support) { onSupportClick(support.id) }
                }
            }
        }

    }
}


@Composable
fun SupportItem(support: Support, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
    ) {
        // Image row
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    end = 12.dp,
                )
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(support.supportImg),
                contentDescription = "Support Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                // Fit, not FillBounds: FillBounds stretched artwork to the fixed
                // 250dp box and distorted every image that was not that exact ratio.
                contentScale = ContentScale.Fit
            )
        }
        // Support tags and color
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 290.dp)
                    .padding(12.dp)
                    .shadow(10.dp, shape = RoundedCornerShape(10.dp), clip = false)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                ,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.tag_icon),
                        contentDescription = "this will be a tag",
                        tint = Color(0xFFf48420),
                        modifier = Modifier
                            .padding(start = 4.dp, top = 8.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = "Tags:  ${support.supportTags.joinToString(", ")}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    SupportColorDots(
                        colorNames = splitColorNames(support.supportColor),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = "Color: ${support.supportColor}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    )
                }
            }
        }
    }
}


/** Splits a support_color value such as "Red, Blue" into its individual names. */
fun splitColorNames(value: String): List<String> =
    value.split(",").map { it.trim() }.filter { it.isNotEmpty() }

/**
 * Colour indicator for a support.
 *
 * One colour draws a single filled dot, exactly as before. A support with several
 * colours draws one dot per colour, each offset by half a diameter so the dot behind
 * shows as a crescent of its own colour. Previously a combined value like "Red, Blue"
 * matched nothing in [getColorFromName] and collapsed to a single grey dot, losing the
 * information entirely.
 */
@Composable
fun SupportColorDots(
    colorNames: List<String>,
    modifier: Modifier = Modifier,
    dotSize: Dp = 16.dp
) {
    if (colorNames.isEmpty()) return

    // Half-diameter step: enough of each underlying dot stays visible to read as its
    // own colour, while the dots still clearly belong to one indicator.
    val step = dotSize / 2

    Box(
        modifier = modifier
            .width(dotSize + step * (colorNames.size - 1))
            .height(dotSize)
    ) {
        colorNames.forEachIndexed { index, name ->
            Icon(
                painter = painterResource(R.drawable.circle_solid_icon),
                // The adjacent "Color: ..." text already names the colours for screen readers.
                contentDescription = null,
                tint = getColorFromName(name),
                modifier = Modifier
                    .size(dotSize)
                    .offset(x = step * index)
            )
        }
    }
}

fun getColorFromName(colorName: String): Color {
    return when (colorName.lowercase()) {
        "red" -> Color.Red
        "blue" -> Color.Blue
        "green" -> Color.Green
        "yellow" -> Color.Yellow
        "orange" -> Color(0xFFFFA500)
        "purple" -> Color(0xFF800080)
        "black" -> Color.Black
        "white" -> Color.White
        "gray", "grey" -> Color.Gray
        else -> Color.DarkGray

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SupportScreenPreview() {
    val sampleSupports = listOf(
        Support(
            id = 1,
            supportImg = "",
            supportColor = "Blue",
            supportTags = listOf("Attacker", "Straw Hat Pirates", "Zoan", "New World")
        ),
        Support(
            id = 2,
            supportImg = "",
            supportColor = "Red",
            supportTags = listOf("Runner", "Straw Hat Pirates", "Paramecia", "New World")
        )
    )

    OpbrcompanionTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier,
                    title = {
                        Text(
                            text = "Support",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                painter = painterResource(R.drawable.tune_icon),
                                tint = Color.Gray,
                                contentDescription = "Filter",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    },
                )
            }
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                LazyColumn {
                    items(sampleSupports.size) { index ->
                        SupportItem(sampleSupports[index])
                    }
                }
            }
        }
    }
}