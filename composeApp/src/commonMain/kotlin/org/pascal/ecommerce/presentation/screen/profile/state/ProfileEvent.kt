package org.pascal.ecommerce.presentation.screen.profile.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.domain.model.Product

val LocalProfileEvent = compositionLocalOf { ProfileEvent() }

@Stable
data class ProfileEvent(
    val onVerified: () -> Unit = {},
    val onLogout: () -> Unit = {},
    val onMaps: () -> Unit = {}
)