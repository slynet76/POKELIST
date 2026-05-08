package com.pokedex.app.ui.detail.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import coil.compose.AsyncImage

/**
 * Static HD artwork with a gentle continuous bob (translateY oscillation)
 * to make Sugimori PNGs feel alive in the detail screen.
 *
 * [phaseOffsetMs] lets two side-by-side images (normal/shiny) bob slightly out of
 * sync so they don't look mechanically identical.
 */
@Composable
fun FloatingArtwork(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    amplitudeDp: Float = 5f,
    durationMs: Int = 1800,
    phaseOffsetMs: Int = 0
) {
    val transition = rememberInfiniteTransition(label = "floatingArtwork")
    val translation by transition.animateFloat(
        initialValue = -amplitudeDp,
        targetValue = amplitudeDp,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMs,
                delayMillis = phaseOffsetMs,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translationY"
    )
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier.graphicsLayer {
            translationY = translation * density
        }
    )
}
