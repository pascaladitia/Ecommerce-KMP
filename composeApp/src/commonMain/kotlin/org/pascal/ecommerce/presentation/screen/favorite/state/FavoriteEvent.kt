package org.pascal.ecommerce.presentation.screen.favorite.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.data.local.entity.FavoriteEntity

val LocalFavoriteEvent = compositionLocalOf { FavoriteEvent() }

@Stable
data class FavoriteEvent(
    val onDelete: (FavoriteEntity?) -> Unit = {},
    val onDetail: (FavoriteEntity?) -> Unit = {}
)