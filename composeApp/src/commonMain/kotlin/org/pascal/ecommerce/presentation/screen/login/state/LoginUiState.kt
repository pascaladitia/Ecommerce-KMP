package org.pascal.ecommerce.presentation.screen.login.state

data class LoginUiState(
    val isLoading: Boolean = false,
    val isError: Pair<Boolean, String> = Pair(false, "")
)