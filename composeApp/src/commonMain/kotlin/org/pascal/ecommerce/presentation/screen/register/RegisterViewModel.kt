package org.pascal.ecommerce.presentation.screen.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.pascal.ecommerce.domain.usecase.auth.AuthUseCase
import org.pascal.ecommerce.presentation.screen.register.state.RegisterUIState
import org.pascal.ecommerce.utils.base.Result

class RegisterViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadRegister(name: String, email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = authUseCase.signInWithEmail(email, password)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRegister = true
                    )
                }
            }

            is Result.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = result.throwable?.message.toString()
                    )
                }
            }
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { it.copy(isRegister = false) }
    }
}