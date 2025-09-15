package org.pascal.ecommerce.presentation.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object LoginScreen: Screen("login")
    data object HomeScreen: Screen("home")
    data object GuideScreen: Screen("guide")
    data object HistoryScreen: Screen("history")
    data object ProfileScreen: Screen("profile")
}