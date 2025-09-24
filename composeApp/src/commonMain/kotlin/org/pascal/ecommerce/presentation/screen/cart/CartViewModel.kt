package org.pascal.ecommerce.presentation.screen.cart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.usecase.local.LocalUseCase
import org.pascal.ecommerce.domain.usecase.transaction.TransactionUseCase
import org.pascal.ecommerce.presentation.screen.cart.state.CartUIState

class CartViewModel(
    private val transactionUseCase: TransactionUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun getCart() {
        val pref = PrefLogin.getLoginResponse()

        _uiState.update { it.copy(isLoading = true) }

        localUseCase.getAllCart()
            .catch {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = it.message
                    )
                }            }
            .collect { result ->
                val filterResult = result.filter { it.userId.toString() == pref?.id }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = filterResult
                    )
                }
            }
    }

    suspend fun deleteCart() {
        _uiState.update { it.copy(isLoading = true) }

        localUseCase.deleteCart()
            .catch {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = it.message
                    )
                }
            }
            .collect {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = emptyList()
                    )
                }
            }
    }


    suspend fun createSnapTransaction(amountInUSD: Double?): String? {
        return try {
            amountInUSD?.let {
                transactionUseCase.createSnapTransaction(it)
            }?.firstOrNull()?.redirectUrl
        } catch (e: Exception) {
            return null
        }
    }

    override fun onCleared() {
        super.onCleared()

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = false,
                product = emptyList()
            )
        }
    }
}