package org.pascal.ecommerce.presentation.screen.home.state

import org.pascal.ecommerce.data.local.entity.ProductEntity

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Pair<Boolean, String> = Pair(false, ""),
    val product: List<ProductEntity>? = null,
    val category: List<String>? = null,
)
