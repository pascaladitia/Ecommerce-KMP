package org.pascal.ecommerce.presentation.screen.login.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalLoginEvent = compositionLocalOf { LoginEvent() }

@Stable
data class LoginEvent(
    val onLogin: (String, String) -> Unit = { _, _ -> }
)