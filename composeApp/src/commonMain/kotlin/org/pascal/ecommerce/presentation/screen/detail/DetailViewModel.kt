package org.pascal.ecommerce.presentation.screen.detail

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.model.Product
import org.pascal.ecommerce.domain.usecase.local.LocalUseCaseImpl
import org.pascal.ecommerce.domain.usecase.product.ProductUseCaseImpl
import org.pascal.ecommerce.presentation.screen.detail.state.DetailUIState
import org.pascal.ecommerce.utils.base.EventAction
import org.pascal.ecommerce.utils.base.sendSuccess
import org.pascal.ecommerce.utils.isOnline
import org.pascal.ecommerce.utils.showToast

class DetailViewModel(
    private val productUseCase: ProductUseCaseImpl,
    private val localUseCase: LocalUseCaseImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _nextEvent = Channel<EventAction<Boolean>>()
    val nextEvent = _nextEvent

    suspend fun loadProductsDetail(id: String?) {
        _uiState.update { it.copy(isLoading = true, product = null) }

        try {
            val favDb = loadFavorite(id)
            val result = if (isOnline()) {
                productUseCase.getProductById(id?.toIntOrNull() ?: 0).firstOrNull().apply {
                    this?.isFavorite = favDb
                }
            } else {
                localUseCase.getProductById(id?.toIntOrNull() ?: 0).firstOrNull().apply {
                    this?.isFavorite = favDb
                }
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
                    message = e.message.orEmpty()
                )
            }
        }
    }

    suspend fun getCart(product: Product?) {
        val pref = PrefLogin.getLoginResponse()

        _uiState.update { it.copy(isLoading = true) }

        try {
            val entity = CartEntity(
                id = product?.id?.toLong() ?: 0L,
                userId = pref?.uid,
                name = product?.title,
                price = product?.price,
                imageID = product?.thumbnail,
                category = product?.category,
                description = product?.description,
                qty = 1
            )

            val result = localUseCase.getCartById(product?.id?.toLong() ?: 0L).firstOrNull()

            if (result == null) {
                loadCart(entity)
            } else {
                loadCart(result.apply { qty = result.qty?.plus(1) })
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

    private suspend fun loadCart(entity: CartEntity) {
        localUseCase.insertCart(entity)
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
                showToast("Success Add to Cart")

                _uiState.update { it.copy(isLoading = false) }
                _nextEvent.sendSuccess(true)
            }
    }

    private suspend fun loadFavorite(id: String?): Boolean {
        try {
            val result = localUseCase.getFavoriteById(id?.toLong() ?: 0).firstOrNull()
            return result != null
        } catch (e: Exception) {
            Logger.e("Tag Favorite", e)
            return false
        }
    }

    suspend fun saveFavorite(isFav: Boolean, product: Product?) {
        try {
            val pref = PrefLogin.getLoginResponse()

            val entity = FavoriteEntity(
                id = product?.id?.toLong() ?: 0L,
                userId = pref?.uid,
                name = product?.title,
                price = product?.price,
                imageID = product?.thumbnail,
                category = product?.category,
                description = product?.description,
                qty = 1
            )

            if (isFav) {
                localUseCase.insertFavorite(entity).collect {  }
            } else {
                localUseCase.deleteFavoriteById(entity).collect {  }
            }
        } catch (e: Exception) {
            Logger.e("Tag Favorite", e)
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }
}