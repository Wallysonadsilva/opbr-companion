package com.guide.opbr_companion.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade

/**
 * Remote image painter with a crossfade on load.
 *
 * Coil 3 moved crossfade off the painter call and onto [ImageRequest], so every
 * call site would otherwise repeat the same four-line builder. The artwork here is
 * fetched over the network and pops in abruptly without it.
 */
@Composable
fun rememberCrossfadePainter(data: Any?): Painter =
    rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(data)
            .crossfade(true)
            .build()
    )
