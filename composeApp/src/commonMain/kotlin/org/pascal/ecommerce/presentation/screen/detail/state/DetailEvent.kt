package org.pascal.ecommerce.presentation.screen.detail.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.domain.model.Product

val LocalDetailEvent = compositionLocalOf { DetailEvent() }

@Stable
data class DetailEvent(
    val onCart: (Product?) -> Unit = {},
    val onFavorite: (Boolean, Product?) -> Unit = { _, _ ->},
    val onNavBack: () -> Unit = {},
)