package org.pascal.ecommerce.presentation.screen.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.pascal.ecommerce.domain.usecase.auth.AuthUseCase
import org.pascal.ecommerce.presentation.screen.register.state.RegisterUIState
import org.pascal.ecommerce.utils.base.EventAction
import org.pascal.ecommerce.utils.base.Result
import org.pascal.ecommerce.utils.base.sendSuccess

class RegisterViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _registerEvent = Channel<EventAction<Boolean>>()
    val registerEvent = _registerEvent

    suspend fun loadRegister(name: String, email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = authUseCase.signUpWithEmail(email, password)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }

                _registerEvent.sendSuccess(true)
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
}