package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.guide.opbr_companion.data.model.Character
import com.guide.opbr_companion.ui.components.FilterBar
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

        if (characters.isEmpty()) {
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
                    color = Color.Gray
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
            .shadow(10.dp, shape = RoundedCornerShape(10.dp), clip = false)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding()
    ) {
        Image(
            painter = rememberImagePainter(character.artwork),
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = character.name,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007FFF),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
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

