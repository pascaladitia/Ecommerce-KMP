package org.pascal.ecommerce.presentation.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object LoginScreen: Screen("login")
    data object RegisterScreen: Screen("register")

    data object HomeScreen: Screen("home")
    data object CartScreen: Screen("live")
    data object FavoriteScreen: Screen("favorite")
    data object ProfileScreen: Screen("profile")

    data object DetailScreen: Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
    data object ReportScreen: Screen("report")
    data object VerifiedScreen: Screen("verified")
}