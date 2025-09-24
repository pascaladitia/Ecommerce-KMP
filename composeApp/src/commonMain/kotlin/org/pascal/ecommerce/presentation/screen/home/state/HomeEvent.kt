package org.pascal.ecommerce.presentation.screen.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.domain.model.Product

val LocalHomeEvent = compositionLocalOf { HomeEvent() }

@Stable
data class HomeEvent(
    val onSearch: (String) -> Unit = {},
    val onCategory: (String) -> Unit = {},
    val onFavorite: (Boolean, Product?) -> Unit = { _, _ ->},
    val onDetail: (Product?) -> Unit = {}
)