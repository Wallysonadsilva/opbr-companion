package com.example.opbr_companion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.opbr_companion.R
import com.example.opbr_companion.ui.viewmodel.CharacterViewModel
import com.example.opbr_companion.data.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    viewModel: CharacterViewModel = viewModel(),
    onBack: () -> Unit
) {
    val character = remember { viewModel.getCharacterById(characterId) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = character?.name ?: "Character Details",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onBack() }
                            .padding(start = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF007FFF)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "All Characters",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF007FFF)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
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
                    painter = rememberImagePainter(character.artwork),
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                SectionWrapper {
                    CharacterInfoSection(character)
                }

                SectionWrapper {
                    MedalSection(character)
                }

                SectionWrapper {
                    RecommendedSetSection(character)
                }

                SectionWrapper(isLast = true) {
                    RecommendedStatsSection(character)
                }

            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Character not found", color = Color.Gray)
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

        // Label + icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Character Tags",
                fontWeight = FontWeight.SemiBold,
            )
            Icon(
                painter = painterResource(R.drawable.tag_icon),
                contentDescription = null,
                tint = Color(0xFFf48420),
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "Color:",
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    painter = painterResource(R.drawable.circle_solid_icon),
                    contentDescription = "Color Icon",
                    tint = getColorFromName(character.color),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(16.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "Class: ",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = character.classType,
                )
            }
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
            Row(verticalAlignment = Alignment.Top) {
                character.medal?.let { medalUrl ->
                    Image(
                        painter = rememberImagePainter(medalUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                character.medalTrait?.let { trait ->
                    Text(text = trait)
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


@Composable
fun RecommendedSetSection(character: Character) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recommended Set",
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        character.recommendedSet?.takeIf { it.isNotEmpty() }?.let { imageUrls ->
            Row {
                imageUrls.forEach { url ->
                    Image(
                        painter = rememberImagePainter(url),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 8.dp)
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