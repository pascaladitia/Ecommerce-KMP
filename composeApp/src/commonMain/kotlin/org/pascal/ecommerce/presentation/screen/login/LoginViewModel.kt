package org.pascal.ecommerce.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.pascal.ecommerce.domain.usecase.auth.AuthUseCase
import org.pascal.ecommerce.presentation.screen.login.state.LoginUiState
import org.pascal.ecommerce.utils.GoogleIdTokenProvider
import org.pascal.ecommerce.utils.base.Result
import org.pascal.ecommerce.utils.base.EventAction
import org.pascal.ecommerce.utils.base.sendSuccess

class LoginViewModel(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _loginEvent = Channel<EventAction<Boolean>>()
    val loginEvent = _loginEvent

    fun setLoading(b: Boolean) = _uiState.update { it.copy(isLoading = b) }
    fun setError(show: Boolean, msg: String = "") =
        _uiState.update { it.copy(isError = show to msg) }

    fun loginEmail(email: String, password: String) = viewModelScope.launch {
        setLoading(true)
        when (val res = authUseCase.signInWithEmail(email, password)) {
            is Result.Success -> {
                setLoading(false)
                _loginEvent.sendSuccess(true)
            }
            is Result.Error -> {
                setError(true, res.throwable?.message ?: "Login gagal")
                setLoading(false)
            }
        }
    }

    fun loginGoogle() = viewModelScope.launch {
        setLoading(true)
        val token = GoogleIdTokenProvider.getTokens()
        if (token?.idToken.isNullOrBlank() || token.accessToken.isBlank()) {
            setError(true, "Google Sign-In dibatalkan")
            setLoading(false); return@launch
        }
        when (val res = authUseCase.signInWithGoogleIdToken(token.idToken, token.accessToken)) {
            is Result.Success -> {
                setLoading(false)
                _loginEvent.sendSuccess(true)
            }
            is Result.Error -> {
                setError(true, res.throwable?.message ?: "Login Google gagal")
                setLoading(false)
            }
        }
    }
}
