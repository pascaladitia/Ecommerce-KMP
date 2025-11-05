package org.pascal.ecommerce.presentation.component.screenUtils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import coil3.disk.DiskCache
import okio.FileSystem

@Composable
fun Modifier.shimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer-transition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "shimmer-translate"
    )

    val shimmerColors = listOf(
        Color.LightGray,
        MaterialTheme.colorScheme.primary,
        Color.LightGray,
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(0f, 0f),
        end = Offset(translateAnim, translateAnim)
    )

    return this.background(brush = brush)
}

fun Modifier.noRippleClickable(onClick: () -> Unit) = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null,
    onClick = onClick
)

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024)
        .build()
}
