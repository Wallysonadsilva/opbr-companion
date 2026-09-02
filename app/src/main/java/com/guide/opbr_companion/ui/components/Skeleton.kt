package com.guide.opbr_companion.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Loading placeholders ("skeletons") for the three list screens.
 *
 * Each skeleton deliberately mirrors the real card's geometry - same corner radii,
 * same padding, same card background ([MaterialTheme.colorScheme.primary]) - so the
 * layout does not shift when the real data arrives.
 *
 * The shimmer tint is derived from the active colour scheme rather than any fixed
 * colour, so it reads correctly in both light and dark themes without adding
 * anything to the app's palette.
 */

private const val SHIMMER_WIDTH = 420f

@Composable
private fun rememberShimmerBrush(): Brush {
    val base = MaterialTheme.colorScheme.onSurface
    val transition = rememberInfiniteTransition(label = "skeleton")
    val offset by transition.animateFloat(
        initialValue = -SHIMMER_WIDTH,
        targetValue = SHIMMER_WIDTH * 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "skeletonOffset"
    )
    return Brush.linearGradient(
        colors = listOf(
            base.copy(alpha = 0.08f),
            base.copy(alpha = 0.20f),
            base.copy(alpha = 0.08f)
        ),
        start = Offset(offset, 0f),
        end = Offset(offset + SHIMMER_WIDTH, 0f)
    )
}

@Composable
private fun ShimmerBlock(
    brush: Brush,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

/** Marks a whole skeleton as one "Loading" node so screen readers announce it once. */
private fun Modifier.loadingSemantics(): Modifier =
    this.semantics { contentDescription = "Loading" }

/**
 * Placeholder for [com.guide.opbr_companion.ui.screen.CharacterScreen]'s 3-column grid.
 * Mirrors CharacterCard: square artwork plus a single name line.
 */
@Composable
fun CharacterGridSkeleton(itemCount: Int = 9) {
    val brush = rememberShimmerBrush()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(26.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .loadingSemantics(),
        userScrollEnabled = false
    ) {
        items(itemCount) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                ShimmerBlock(
                    brush = brush,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ShimmerBlock(
                        brush = brush,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * Placeholder for [com.guide.opbr_companion.ui.screen.SupportScreen]'s list.
 * Mirrors SupportItem: a tall banner image above the tag/colour block.
 */
@Composable
fun SupportListSkeleton(itemCount: Int = 3) {
    val brush = rememberShimmerBrush()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .loadingSemantics(),
        userScrollEnabled = false
    ) {
        items(itemCount) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    ShimmerBlock(
                        brush = brush,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .widthIn(max = 290.dp)
                        .padding(12.dp)
                ) {
                    ShimmerBlock(
                        brush = brush,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                    ShimmerBlock(
                        brush = brush,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(0.6f)
                            .height(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Placeholder for [com.guide.opbr_companion.ui.screen.MedalScreen]'s list.
 * Mirrors MedalCard: title, divider, three medal rows, divider, footer link.
 */
@Composable
fun MedalListSkeleton(itemCount: Int = 4) {
    val brush = rememberShimmerBrush()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .loadingSemantics(),
        userScrollEnabled = false
    ) {
        items(itemCount) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(20.dp)
            ) {
                ShimmerBlock(
                    brush = brush,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(18.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(12.dp))

                repeat(3) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        ShimmerBlock(
                            brush = brush,
                            modifier = Modifier.size(60.dp),
                            shape = CircleShape
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        ShimmerBlock(
                            brush = brush,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(12.dp))

                ShimmerBlock(
                    brush = brush,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(14.dp)
                )
            }
        }
    }
}
