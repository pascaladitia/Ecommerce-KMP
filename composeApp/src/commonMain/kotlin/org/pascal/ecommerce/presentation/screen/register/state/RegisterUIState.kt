package org.pascal.ecommerce.presentation.screen.register.state

data class RegisterUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isRegister: Boolean = false
)
