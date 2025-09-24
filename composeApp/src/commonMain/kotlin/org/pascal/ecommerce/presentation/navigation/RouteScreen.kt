package org.pascal.ecommerce.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.pascal.ecommerce.presentation.screen.login.LoginScreen
import org.pascal.ecommerce.presentation.screen.splash.SplashScreen
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.presentation.screen.detail.DetailScreen
import org.pascal.ecommerce.presentation.screen.home.HomeScreen

@Composable
fun RouteScreen(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.HomeScreen.route,
                    Screen.FavoriteScreen.route,
                    Screen.CartScreen.route,
                    Screen.ProfileScreen.route
                )
            ) {
                BottomBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.SplashScreen.route
        ) {
            composable(route = Screen.SplashScreen.route) {
                SplashScreen(
                    paddingValues = paddingValues
                ) {
                    val isLogin = PrefLogin.getIsLogin()

                    navController.navigate(
                        if (isLogin) Screen.HomeScreen.route else Screen.LoginScreen.route
                    ) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
            composable(route = Screen.LoginScreen.route) {
                LoginScreen(
                    onLogin = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("id", it)
                        navController.navigate(Screen.DetailScreen.route)
                    }
                )
            }
            composable(route = Screen.DetailScreen.route) {
                DetailScreen(
                    paddingValues = paddingValues,
                    productId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("id"),
                    onNavBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
