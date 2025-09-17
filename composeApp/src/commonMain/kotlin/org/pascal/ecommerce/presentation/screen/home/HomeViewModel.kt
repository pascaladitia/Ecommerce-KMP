package org.pascal.ecommerce.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.preferences.PrefCategories
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.usecase.local.LocalUseCase
import org.pascal.ecommerce.domain.usecase.product.ProductUseCase
import org.pascal.ecommerce.presentation.screen.home.state.HomeUIState

class HomeViewModel(
    private val productUseCase: ProductUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline get() = _isOnline.asStateFlow()

    fun loadProduct(name: String = "") {
        setLoading(true)

        viewModelScope.launch {
            try {
                val favDb = loadFavorite().orEmpty()

                val result = if (isOnline.value) {
                    if (name.isEmpty()) {
                        productUseCase.getProduct().firstOrNull()?.products.orEmpty()
                    } else {
                        productUseCase.getProductByCategory(name).firstOrNull()?.products.orEmpty()
                    }
                } else {
                    localUseCase.getAllProduct().firstOrNull().orEmpty()
                }

                val product = withContext(Dispatchers.Default) {
                    result.map { it.copy(isFavorite = favDb.any { favId -> favId == it.id }) }
                }

                if (isOnline.value) saveLocalProduct(product)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = product,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = Pair(true, e.message.toString())
                    )
                }
            }
        }
    }

    suspend fun loadCategory() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = if (isOnline.value) productUseCase.getCategories().firstOrNull()
            else PrefCategories.getCategoriesResponse()

            if (isOnline.value) PrefCategories.setCategoriesResponse(result)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    category = result
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = Pair(true, e.message.toString())
                )
            }
        }
    }

    fun searchProduct(name: String) {
        val result = _uiState.value.product?.filter { product ->
            product.title?.contains(name, ignoreCase = true) ?: false
        }

        _uiState.update {
            it.copy(
                product = result
            )
        }
    }

    private suspend fun loadFavorite(): List<Int>? {
        val pref = PrefLogin.getLoginResponse()
        return localUseCase.getAllFavorite()
            .firstOrNull()
            ?.filter { it.userId.toString() == pref?.id }
            ?.map { it.id.toInt() }
    }

    private suspend fun saveLocalProduct(product: List<ProductEntity>) {
        localUseCase.deleteProduct().collect()
        product.forEach {
            localUseCase.insertProduct(it).collect()
        }
    }

    suspend fun saveFavorite(isFav: Boolean, product: ProductEntity?) {
        try {
            val pref = PrefLogin.getLoginResponse()

            val entity = FavoriteEntity(
                id = product?.id?.toLong() ?: 0L,
                userId = pref?.id,
                name = product?.title,
                price = product?.price,
                imageID = product?.thumbnail,
                category = product?.category,
                description = product?.description,
                qty = 1
            )

            if (isFav) {
                localUseCase.insertFavorite(entity).collect()
            } else {
                localUseCase.deleteFavoriteById(entity).collect()
            }
        } catch (e: Exception) {
            Logger.e("Tag Favorite " + e.message.toString())
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun setError(isError: Boolean) {
        _uiState.update { it.copy(isError = Pair(isError, "")) }
    }
}