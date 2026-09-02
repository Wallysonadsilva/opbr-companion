package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.guide.opbr_companion.data.model.MedalSet
import com.guide.opbr_companion.ui.components.FilterBar
import com.guide.opbr_companion.ui.components.MedalListSkeleton
import com.guide.opbr_companion.ui.components.MedalDetailSheet
import com.guide.opbr_companion.ui.viewmodel.MedalViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedalScreen(
    viewModel: MedalViewModel = viewModel(),
    showFilterBar: Boolean,
) {
    val medals = viewModel.filterMedalList.collectAsState().value
    val filterState = viewModel.filterState.collectAsState().value
    val allTags = viewModel.allTags
    val isLoading = viewModel.isLoading.collectAsState().value

    var selectedMedal by remember { mutableStateOf<MedalSet?>(null) }

    selectedMedal?.let {
        MedalDetailSheet(
            medalSet = it,
            onDismiss = { selectedMedal = null }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showFilterBar) {
            FilterBar(
                labelText = "Tags",
                selectedTags = filterState.selectedTags,
                availableTags = allTags.toSet(),
                onTagToggled = { tag ->
                    val updatedTags = filterState.selectedTags.toMutableSet().apply {
                        if (contains(tag)) remove(tag) else add(tag)
                    }
                    viewModel.updateFilter(updatedTags)
                },
                centerTags = true
            )
        }

        if (isLoading) {
            MedalListSkeleton()
        } else if (medals.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "No medal sets found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(medals.size) { index ->
                    MedalCard(medals[index]) {
                        selectedMedal = medals[index]
                    }
                }
            }
        }
    }
}


@Composable
fun MedalCard(medalSet: MedalSet, onClick: () -> Unit) {
    val medalIcons = medalSet.medals
    val descriptions = medalSet.medalTraits

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Text(
            text = medalSet.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(12.dp))

        medalIcons.forEachIndexed { index, iconUrl ->
            val traitText = descriptions.getOrNull(index) ?: ""
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Fixed 60dp medal. weight(0.3f) previously overrode size(60.dp), so the
                // medal was sized by column share and grew or shrank with screen width.
                Image(
                    painter = rememberAsyncImagePainter(iconUrl),
                    contentDescription = "Medal Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = traitText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
        ) {
            Text(
                text = "Set Detail",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}