package org.pascal.ecommerce.presentation.screen.maps

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.pascal.ecommerce.domain.usecase.local.LocalUseCaseImpl
import org.pascal.ecommerce.domain.usecase.order.OrderUseCaseImpl
import org.pascal.ecommerce.presentation.screen.maps.state.MapsUIState

class MapsViewModel(
    private val transactionUseCase: OrderUseCaseImpl,
    private val localUseCase: LocalUseCaseImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapsUIState())
    val uiState get() = _uiState.asStateFlow()

}