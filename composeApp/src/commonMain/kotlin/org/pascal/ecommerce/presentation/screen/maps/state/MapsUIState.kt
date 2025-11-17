package org.pascal.ecommerce.presentation.screen.maps.state

import org.pascal.ecommerce.data.local.entity.CartEntity

data class MapsUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = ""
)
