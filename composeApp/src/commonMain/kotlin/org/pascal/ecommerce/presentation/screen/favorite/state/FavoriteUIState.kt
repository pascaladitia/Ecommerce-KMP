package org.pascal.ecommerce.presentation.screen.favorite.state

import org.pascal.ecommerce.data.local.entity.FavoriteEntity

data class FavoriteUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<FavoriteEntity>? = null
)
