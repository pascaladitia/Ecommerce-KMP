package org.pascal.ecommerce.presentation.screen.detail.state

import org.pascal.ecommerce.domain.model.Product

data class DetailUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false,
    val product: Product? = null
)