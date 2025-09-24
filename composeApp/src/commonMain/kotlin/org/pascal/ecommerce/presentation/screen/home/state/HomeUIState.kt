package org.pascal.ecommerce.presentation.screen.home.state

import org.pascal.ecommerce.domain.model.Product

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Pair<Boolean, String> = Pair(false, ""),
    val product: List<Product>? = null,
    val category: List<String>? = null,
)
