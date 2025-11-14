package org.pascal.ecommerce.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.usecase.auth.AuthUseCase
import org.pascal.ecommerce.domain.usecase.transaction.TransactionUseCase
import org.pascal.ecommerce.presentation.screen.profile.state.ProfileUIState
import org.pascal.ecommerce.utils.base.Result

class ProfileViewModel(
    private val transactionUseCase: TransactionUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadVerified(id: String) {
        when (val result = transactionUseCase.isUserVerified(id)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(
                        isVerified = result.data
                    )
                }
            }

            is Result.Error -> {
                Logger.e("SignOut", result.throwable)
            }
        }
    }

    suspend fun loadTransaction(id: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = transactionUseCase.getTransactionsByUserId(id)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        transactionList = result.data
                    )
                }
            }

            is Result.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = it.message
                    )
                }
            }
        }
    }

    fun loadLogout() = viewModelScope.launch {
        try {
            PrefLogin.setIsLogin(false)
            authUseCase.signOut()
        } catch (e: Exception) {
            Logger.e("SignOut", e)
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }
}