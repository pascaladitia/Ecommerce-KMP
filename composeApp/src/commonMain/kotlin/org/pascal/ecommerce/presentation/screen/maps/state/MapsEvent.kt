package org.pascal.ecommerce.presentation.screen.maps.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalMapsEvent = compositionLocalOf { MapsEvent() }

@Stable
data class MapsEvent(
    val onNavBack: () -> Unit = {},
)