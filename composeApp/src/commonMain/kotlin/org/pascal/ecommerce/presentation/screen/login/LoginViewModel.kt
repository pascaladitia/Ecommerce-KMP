package org.pascal.ecommerce.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.model.BaseProduct
import org.pascal.ecommerce.domain.usecase.ProductUseCase
import org.pascal.ecommerce.utils.base.EventAction
import org.pascal.ecommerce.utils.base.UiState
import org.pascal.ecommerce.utils.base.UiState.Companion.default
import org.pascal.ecommerce.utils.base.sendFailure
import org.pascal.ecommerce.utils.base.sendLoading
import org.pascal.ecommerce.utils.base.sendSuccess

class LoginViewModel(
    private val productUseCase: ProductUseCase,
) : ViewModel() {

    private val _loginEvent = Channel<EventAction<Boolean>>()
    val loginEvent = _loginEvent

    private val _postApiState = MutableStateFlow<UiState<BaseProduct>>(default())
    val postApiState: StateFlow<UiState<BaseProduct>> = _postApiState

    suspend fun exeLogin(username: String, password: String) {
        _loginEvent.sendLoading()

        if (username == "test" && password == "123456") {
            PrefLogin.setIsLogin(true)
            _loginEvent.sendSuccess(true)
        } else {
            _loginEvent.sendFailure("Username atau password salah")
        }
    }

    fun postApi() {
        viewModelScope.launch {
            _postApiState.value = UiState.loading()

            productUseCase.getProductByCategory("")
                .catch {
                    _postApiState.value = UiState.fail(it, it.message)
                }.collect {
                    _postApiState.value = UiState.success(it)
                }
        }
    }
}