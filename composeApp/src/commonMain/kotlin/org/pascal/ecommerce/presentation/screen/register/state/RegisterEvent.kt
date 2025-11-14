package org.pascal.ecommerce.presentation.screen.register.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalRegisterEvent = compositionLocalOf { RegisterEvent() }

@Stable
data class RegisterEvent(
    val onRegister: (String, String, String) -> Unit = { _, _, _ ->},
    val onNavBack: () -> Unit = {},
)