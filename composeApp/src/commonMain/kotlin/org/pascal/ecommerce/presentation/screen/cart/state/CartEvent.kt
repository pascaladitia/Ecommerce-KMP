package org.pascal.ecommerce.presentation.screen.cart.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.data.local.entity.CartEntity

val LocalCartEvent = compositionLocalOf { CartEvent() }

@Stable
data class CartEvent(
    val onNext: (List<CartEntity?>) -> Unit = {},
    val onDelete: () -> Unit = {},
)