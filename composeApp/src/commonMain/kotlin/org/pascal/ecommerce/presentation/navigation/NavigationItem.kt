package org.pascal.ecommerce.presentation.navigation

import org.jetbrains.compose.resources.DrawableResource

data class NavigationItem(
    val title: String,
    val icon: DrawableResource,
    val screen: Screen
)
