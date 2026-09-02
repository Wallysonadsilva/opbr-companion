package com.guide.opbr_companion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guide.opbr_companion.ui.components.rememberCrossfadePainter
import com.guide.opbr_companion.R
import com.guide.opbr_companion.data.model.Support
import com.guide.opbr_companion.ui.viewmodel.SupportViewModel

/**
 * Detail view for a single support, opened by tapping a card on the support list.
 *
 * Layout: the support artwork, then two summary cards (main colour / total tags),
 * then the full tag list as a responsive grid.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportDetailScreen(
    supportId: Int,
    viewModel: SupportViewModel,
    onBack: () -> Unit
) {
    // Observed rather than snapshotted, for the same reason as the character
    // screen: getSupportById reads a list that Supabase fills in asynchronously,
    // so keying on supportId alone still caches a null taken before the fetch
    // landed and never looks again.
    val supports by viewModel.supportList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val support = remember(supports, supportId) { supports.find { it.id == supportId } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Support Details",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to support list",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        if (support == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "Support not found",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // The height is pinned deliberately. Support artwork varies in aspect ratio, so
            // a fixed-height box scaled with Fit frames every image identically instead of
            // letting each one set its own height and shift the rest of the layout.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberCrossfadePainter(support.supportImg),
                    contentDescription = "Support image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SupportSummaryRow(support)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Associated Tags",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            SupportTagGrid(tags = support.supportTags)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/** The two side-by-side summary cards, kept to matching heights. */
@Composable
private fun SupportSummaryRow(support: Support) {
    // support_color can hold more than one colour, e.g. "Red, Blue".
    val colours = support.supportColor
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    val tagCount = support.supportTags.size

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        SummaryCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            SummaryHeading(iconRes = R.drawable.paint_icon, label = "Main Color")

            Spacer(modifier = Modifier.height(12.dp))

            colours.forEach { colour ->
                val colourTint = readableColorFor(colour)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.circle_solid_icon),
                        contentDescription = null,
                        tint = colourTint,
                        modifier = Modifier.size(16.dp)
                    )
                    // Same size and weight as the tag count opposite it, so the two
                    // summary cards read as a matched pair.
                    Text(
                        text = colour,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colourTint,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        SummaryCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            SummaryHeading(iconRes = R.drawable.tag_icon, label = "Total Tags")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (tagCount == 1) "1 tag" else "$tagCount tags",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFf48420)
            )
        }
    }
}

/**
 * Readable text colour for a support colour name.
 *
 * The saturated values used for the small dots on the list would not survive as text:
 * pure blue on the dark card is about 1.6:1 contrast. These are the Material 700 shades
 * on light and the 300 shades on dark, which all clear the 3:1 WCAG AA bar for large
 * bold text in both themes while still reading as the colour they name.
 */
@Composable
private fun readableColorFor(colorName: String): Color {
    val dark = isSystemInDarkTheme()
    return when (colorName.trim().lowercase()) {
        "red" -> if (dark) Color(0xFFE57373) else Color(0xFFD32F2F)
        "blue" -> if (dark) Color(0xFF64B5F6) else Color(0xFF1976D2)
        "green" -> if (dark) Color(0xFF81C784) else Color(0xFF388E3C)
        "yellow" -> if (dark) Color(0xFFFFD54F) else Color(0xFFF9A825)
        "purple" -> if (dark) Color(0xFFBA9BDD) else Color(0xFF7B1FA2)
        "light" -> if (dark) Color(0xFFE0E0E0) else Color(0xFF757575)
        "dark" -> if (dark) Color(0xFFB0BEC5) else Color(0xFF37474F)
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
private fun SummaryHeading(iconRes: Int, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color(0xFFf48420),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        content = content
    )
}

/**
 * Tag cards laid out two per row, going to three once the screen is wide enough.
 *
 * Rows are built by hand rather than with a LazyVerticalGrid because this sits inside
 * a vertically scrolling Column, where a nested lazy grid would be measured with an
 * unbounded height and crash.
 */
@Composable
private fun SupportTagGrid(tags: List<String>) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        // Two columns on a phone; three only when each card still has room for a long
        // tag such as "The Seven Warlords of the Sea".
        val columns = if (maxWidth >= 520.dp) 3 else 2

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            tags.chunked(columns).forEach { rowTags ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    rowTags.forEach { tag ->
                        TagCard(
                            tag = tag,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                    // Keeps a short final row's cards the same width as the rows above.
                    repeat(columns - rowTags.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun TagCard(tag: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
