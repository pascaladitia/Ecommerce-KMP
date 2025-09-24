package org.pascal.ecommerce.presentation.screen.cart.state

import org.pascal.ecommerce.data.local.entity.CartEntity

data class CartUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<CartEntity> = emptyList()
)
