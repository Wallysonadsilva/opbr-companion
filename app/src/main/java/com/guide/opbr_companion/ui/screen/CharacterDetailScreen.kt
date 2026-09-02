package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guide.opbr_companion.ui.components.rememberCrossfadePainter
import com.guide.opbr_companion.R
import com.guide.opbr_companion.ui.components.AlternativeSetsDialog
import com.guide.opbr_companion.ui.viewmodel.CharacterViewModel
import com.guide.opbr_companion.data.model.Character

/**
 * Helper function to get color from string name with null safety.
 *
 * Not private: the grid card shows the same swatch, and two copies of this
 * mapping would drift the first time a colour is added.
 */
internal fun getColorFromName(colorName: String?): Color =
    characterColorOrNull(colorName) ?: Color.Gray

/**
 * The same mapping, but honest about not recognising a value.
 *
 * A grey dot is indistinguishable from a real colour at 8dp, so anywhere the
 * swatch is the only thing being shown — the grid card — needs to know the
 * difference between "grey" and "no idea" and simply leave it out.
 */
internal fun characterColorOrNull(colorName: String?): Color? =
    when (colorName?.lowercase()?.trim()) {
        "red" -> Color(0xFFD32F2F)
        "blue" -> Color(0xFF1976D2)
        "green" -> Color(0xFF388E3C)
        "yellow" -> Color(0xFFFBC02D)
        "purple" -> Color(0xFF7B1FA2)
        else -> null
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    viewModel: CharacterViewModel = viewModel(),
    onBack: () -> Unit
) {
    // Observed, not snapshotted. `remember { getCharacterById() }` read the list
    // once at first composition and cached whatever it found — which is null
    // whenever the Supabase fetch has not landed yet, and stayed null forever
    // afterwards. Anyone arriving here after process death, on a slow
    // connection, or before the first load finished got a permanent
    // "Character not found" on a character that exists.
    val characters by viewModel.characterList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val character = remember(characters, characterId) {
        characters.find { it.id == characterId }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = character?.name ?: "Character Details",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    // Icon-only back control. The old "All Characters" label ate a large
                    // slice of the bar, squeezing the centred character name; an IconButton
                    // still gives the full 48dp touch target.
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to all characters",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        if (character != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Image(
                    painter = rememberCrossfadePainter(character.artwork),
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                // A section with nothing in it used to still emit its spacers and
                // its divider, so a character with no medal data showed a rule
                // across empty space. Each section now declares whether it has
                // anything to show, and the last one present drops its divider.
                val hasMedal = !character.medal.isNullOrEmpty() ||
                        !character.medalTrait.isNullOrEmpty() ||
                        !character.medalTags.isNullOrEmpty()
                val hasStats = !character.recommendStats.isNullOrEmpty() ||
                        !character.statMessage.isNullOrEmpty()

                SectionWrapper {
                    CharacterInfoSection(character)
                }

                if (hasMedal) {
                    SectionWrapper {
                        MedalSection(character)
                    }
                }

                SectionWrapper(isLast = !hasStats) {
                    RecommendedSetSection(character)
                }

                if (hasStats) {
                    SectionWrapper(isLast = true) {
                        RecommendedStatsSection(character)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                // Still loading and genuinely absent are different answers, and
                // telling someone their character does not exist while the list
                // is still arriving is the wrong one.
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "Character not found",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SectionWrapper(
    isLast: Boolean = false,
    content: @Composable () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        content()

        Spacer(modifier = Modifier.height(16.dp))

        if (!isLast) {
            HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.2f))
        }
    }
}


@Composable
fun CharacterInfoSection(character: Character) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        character.title?.let { title ->
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Colour and class as their own metadata line. These previously shared one
        // non-wrapping Row with the "Character Tags" heading, which ran past the screen
        // edge on phone widths and clipped the class value off the right side.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Color:", fontWeight = FontWeight.SemiBold)
            Icon(
                painter = painterResource(R.drawable.circle_solid_icon),
                contentDescription = "Color: " + character.color,
                tint = getColorFromName(character.color),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(16.dp)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(text = "Class:", fontWeight = FontWeight.SemiBold)
            Text(
                text = character.classType,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tag icon leads its label so the two read as a single heading unit.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.tag_icon),
                contentDescription = null,
                tint = Color(0xFFf48420),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Character Tags",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        character.characterTags?.forEach { tag ->
            Text(
                text = "• $tag",
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun MedalSection(character: Character) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Medal + trait
        if (!character.medal.isNullOrEmpty() || !character.medalTrait.isNullOrEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                character.medal?.let { medalUrl ->
                    Image(
                        painter = rememberCrossfadePainter(medalUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                character.medalTrait?.let { trait ->
                    // weight() keeps the trait wrapping in the space next to the medal
                    // rather than running off the right edge on longer descriptions.
                    Text(text = trait, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Medal Tags
        character.medalTags?.takeIf { it.isNotEmpty() }?.let { tags ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Medal Tags ", fontWeight = FontWeight.SemiBold)
                Icon(
                    painter = painterResource(R.drawable.tag_icon),
                    contentDescription = null,
                    tint = Color(0xFFf48420),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                tags.forEach { tag ->
                    Text("• $tag", fontSize = 14.sp)
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecommendedSetSection(character: Character) {
    var showAltSets by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recommended Set",
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        character.recommendedSet?.takeIf { it.isNotEmpty() }?.let { imageUrls ->
            // FlowRow wraps to a second line instead of pushing later medals off
            // screen when a set has more icons than fit across one row.
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                imageUrls.forEach { url ->
                    Image(
                        painter = rememberCrossfadePainter(url),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        character.setMessage?.let {
            Text(
                text = it,
                textAlign = TextAlign.Justify
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.recommended_set_note),
            textAlign = TextAlign.Justify
        )

        // Only characters that actually have alternatives get the button.
        character.altSets?.takeIf { it.isNotEmpty() }?.let { altSets ->
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showAltSets = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFf48420),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "View Alternative Sets",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            if (showAltSets) {
                AlternativeSetsDialog(
                    altSets = altSets,
                    onDismiss = { showAltSets = false }
                )
            }
        }
    }
}


@Composable
fun RecommendedStatsSection(character: Character) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        character.recommendStats?.let { stats ->
            Text(
                text = "Recommended Stats: $stats",
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        character.statMessage?.let { message ->
            Text(text = message)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.Recommended_stats_note),
            textAlign = TextAlign.Justify
        )
    }
}