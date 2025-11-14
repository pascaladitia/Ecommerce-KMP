package org.pascal.ecommerce.presentation.screen.favorite

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.usecase.local.LocalUseCaseImpl
import org.pascal.ecommerce.domain.usecase.product.ProductUseCaseImpl
import org.pascal.ecommerce.presentation.screen.favorite.state.FavoriteUIState

@KoinViewModel
class FavoriteViewModel(
    private val productUseCase: ProductUseCaseImpl,
    private val localUseCase: LocalUseCaseImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadFavorite() {
        val pref = PrefLogin.getLoginResponse()

        _uiState.update { it.copy(isLoading = true, product = null) }

        try {
            val result = localUseCase.getAllFavorite().firstOrNull()?.filter {
                it.userId.toString() == pref?.uid
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = result
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    message = e.message.toString()
                )
            }
        }
    }

    suspend fun delete(product: FavoriteEntity?) {
        product?.let {
            localUseCase.deleteFavoriteById(it)
                .catch {
                    Logger.e("Tag Favorite", it)
                }
                .collect {
                    loadFavorite()
                }
        }
    }

    fun setError(boolean: Boolean) {
        _uiState.update { it.copy(isError = boolean) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update {
            it.copy(
                isLoading = false,
                isError = false
            )
        }
    }
}