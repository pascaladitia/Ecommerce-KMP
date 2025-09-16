package org.pascal.ecommerce.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.model.BaseProduct
import org.pascal.ecommerce.domain.usecase.local.LocalUseCase
import org.pascal.ecommerce.domain.usecase.product.ProductUseCase
import org.pascal.ecommerce.presentation.screen.home.state.HomeUIState
import org.pascal.ecommerce.utils.base.UiState
import org.pascal.ecommerce.utils.base.UiState.Companion.default

class HomeViewModel(
    private val productUseCase: ProductUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState get() = _uiState.asStateFlow()

    private val _postApiState = MutableStateFlow<UiState<BaseProduct>>(default())
    val postApiState: StateFlow<UiState<BaseProduct>> = _postApiState

    private val _isOnline = MutableStateFlow(true)
    val isOnline get() = _isOnline.asStateFlow()

    fun loadProduct() {
        setLoading(true)

        viewModelScope.launch {
            val favDb = loadFavorite()

            productUseCase.getProductByCategory("")
                .catch {
                    _postApiState.value = UiState.fail(it, it.message)
                }.collect {
                    _postApiState.value = UiState.success(it)
                }
        }
    }

    private suspend fun loadFavorite(): List<Int>? {
        val pref = PrefLogin.getLoginResponse()
        return localUseCase.getAllFavorite()
            .firstOrNull()
            ?.filter { it.userId.toString() == pref?.id }
            ?.map { it.id.toInt() }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun setError(isError: Boolean) {
        _uiState.update { it.copy(isError = Pair(isError, "")) }
    }
}