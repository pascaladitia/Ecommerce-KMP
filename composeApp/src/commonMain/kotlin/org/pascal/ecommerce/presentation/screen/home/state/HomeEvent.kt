package org.pascal.ecommerce.presentation.screen.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.data.local.entity.ProductEntity

val LocalHomeEvent = compositionLocalOf { HomeEvent() }

@Stable
data class HomeEvent(
    val onSearch: (String) -> Unit = {},
    val onCategory: (String) -> Unit = {},
    val onFavorite: (Boolean, ProductEntity?) -> Unit = { _, _ ->},
    val onDetail: (ProductEntity?) -> Unit = {}
)