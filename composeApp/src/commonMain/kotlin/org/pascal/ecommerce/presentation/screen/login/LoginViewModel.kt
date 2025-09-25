package org.pascal.ecommerce.presentation.screen.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.usecase.product.ProductUseCase
import org.pascal.ecommerce.presentation.screen.login.state.LoginUiState
import org.pascal.ecommerce.utils.base.EventAction
import org.pascal.ecommerce.utils.base.sendSuccess

class LoginViewModel(
    private val productUseCase: ProductUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _loginEvent = Channel<EventAction<Boolean>>()
    val loginEvent = _loginEvent

    suspend fun exeLogin(username: String, password: String) {
        setLoading(true)

        if (username == "test" && password == "123456") {
            setLoading(false)
            PrefLogin.setIsLogin(true)

            _loginEvent.sendSuccess(true)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = Pair(true, "Username atau password salah")
                )
            }
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun setError(isError: Boolean) {
        _uiState.update { it.copy(isError = Pair(isError, "")) }
    }
}