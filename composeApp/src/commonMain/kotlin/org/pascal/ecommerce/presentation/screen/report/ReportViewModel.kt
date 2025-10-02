package org.pascal.ecommerce.presentation.screen.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.domain.usecase.local.LocalUseCase
import org.pascal.ecommerce.domain.usecase.transaction.TransactionUseCase
import org.pascal.ecommerce.presentation.screen.report.state.ReportUIState
import org.pascal.ecommerce.utils.ReportInfo
import org.pascal.ecommerce.utils.base.Result
import org.pascal.ecommerce.utils.currentFormattedDate
import org.pascal.ecommerce.utils.generatePdfAndOpen
import org.pascal.ecommerce.utils.tryLoadLogoBytes

class ReportViewModel(
    private val localUseCase: LocalUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadReport(product: List<CartEntity>?) {
        val pref = PrefLogin.getLoginResponse()

        _uiState.update { it.copy(isLoading = true) }

        when (val result = transactionUseCase.addTransaction(pref, product)) {
            is Result.Success -> {
                Logger.d(result.data.toString())
            }

            is Result.Error -> {
                Logger.e("tag report", result.throwable)
            }
        }
    }

    suspend fun deleteCart() {
        try {
            localUseCase.deleteCart().collect {  }
        } catch (e: Exception) {
            Logger.e("tag report", e)
        }
    }

    fun generateReport(products: List<CartEntity>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                val pref = PrefLogin.getLoginResponse()
                val info = ReportInfo(pref?.displayName.orEmpty(), pref?.email.orEmpty())
                val fileName = "Report_${currentFormattedDate()}.pdf"
                val logoBytes = tryLoadLogoBytes()

                generatePdfAndOpen(
                    products = products,
                    reportInfo = info,
                    fileName = fileName,
                    logoBytes = logoBytes
                )
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, isError = true, message = e.message.toString()) }
            }
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { it.copy(isReport = false) }
    }
}