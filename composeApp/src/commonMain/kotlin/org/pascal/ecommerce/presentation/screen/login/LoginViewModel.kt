package org.pascal.ecommerce.presentation.screen.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.data.repository.Repository
import org.pascal.ecommerce.utils.UiState

class LoginViewModel(
    private val repository: Repository,
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val loginState: StateFlow<UiState<Boolean>> = _loginState

    fun resetDialog() {
        _loginState.value = UiState.Error("")
    }

    fun exeLogin(username: String, password: String) {
        if (username == "test" && password == "123456") {
            PrefLogin.setIsLogin(true)
            _loginState.value = UiState.Success(true)
        } else {
            _loginState.value = UiState.Error("Username atau password salah")
        }
    }
}