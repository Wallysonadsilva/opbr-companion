package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.guide.opbr_companion.data.model.Character
import com.guide.opbr_companion.ui.components.FilterBar
import com.guide.opbr_companion.ui.components.CharacterGridSkeleton
import com.guide.opbr_companion.ui.viewmodel.CharacterViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = viewModel(),
    showFilterBar: Boolean,
    onCharacterClick: (Int) -> Unit
) {
    val characters by viewModel.filteredCharecterList.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val allTags = viewModel.allTags
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (showFilterBar) {
            FilterBar(
                labelText = "Class",
                selectedTags = filterState.selectedTags,
                availableTags = allTags.toSet(),
                onTagToggled = { tag ->
                    val updatedTags = filterState.selectedTags.toMutableSet().apply {
                        if (contains(tag)) remove(tag) else add(tag)
                    }
                    viewModel.updateFilter(updatedTags, null)
                },
                centerTags = true
            )
        }

        if (isLoading) {
            CharacterGridSkeleton()
        } else if (characters.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "No characters data found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(26.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(characters.size) { index ->
                    CharacterCard(characters[index]) {
                        onCharacterClick(characters[index].id)
                    }

                }
            }
        }
    }
}


@Composable
fun CharacterCard(character: Character, onCharacterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onCharacterClick() }
            .shadow(6.dp, shape = RoundedCornerShape(12.dp), clip = false)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Image(
            painter = rememberAsyncImagePainter(character.artwork),
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop
        )
        // Fixed-height name slot: long names used to wrap and make cards in the same
        // grid row different heights. Two lines with an ellipsis keeps the grid even
        // while still showing far more of a long name than a single line would.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .padding(horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = character.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Colour and class, the two things anyone scans a roster for. Both were
        // already being fetched and shown on the detail screen, so the grid was
        // asking people to open a character to learn what it is. Fixed height,
        // so the cards in a row stay level.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            characterColorOrNull(character.color)?.let { swatch ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(swatch)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = character.classType,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CharacterScreenPreview() {
    val sampleCharacters = listOf(
        Character(
            1,
            "",
            "Attacker",
            "Red",
            "Sakazuki",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ),
        Character(
            2,
            "",
            "Defender",
            "Blue",
            "Rob Lucci",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ),
        Character(
            3,
            "",
            "Runner",
            "Green",
            "Soba Mask",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(26.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(sampleCharacters) { character ->
            CharacterCard(character, onCharacterClick = {})
        }
    }
}

