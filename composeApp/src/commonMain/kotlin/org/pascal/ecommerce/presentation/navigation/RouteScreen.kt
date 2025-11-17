package org.pascal.ecommerce.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.presentation.screen.cart.CartScreen
import org.pascal.ecommerce.presentation.screen.detail.DetailScreen
import org.pascal.ecommerce.presentation.screen.favorite.FavoriteScreen
import org.pascal.ecommerce.presentation.screen.home.HomeScreen
import org.pascal.ecommerce.presentation.screen.login.LoginScreen
import org.pascal.ecommerce.presentation.screen.maps.MapsScreen
import org.pascal.ecommerce.presentation.screen.profile.ProfileScreen
import org.pascal.ecommerce.presentation.screen.register.RegisterScreen
import org.pascal.ecommerce.presentation.screen.report.ReportScreen
import org.pascal.ecommerce.presentation.screen.splash.SplashScreen
import org.pascal.ecommerce.utils.base.getFromPreviousBackStack
import org.pascal.ecommerce.utils.base.saveToCurrentBackStack

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
                    },
                    onRegister = {
                        navController.navigate(Screen.RegisterScreen.route)
                    }
                )
            }
            composable(route = Screen.RegisterScreen.route) {
                RegisterScreen(
                    paddingValues = paddingValues,
                    onNavBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        saveToCurrentBackStack(navController, "id", it)
                        navController.navigate(Screen.DetailScreen.route)
                    }
                )
            }
            composable(route = Screen.CartScreen.route) {
                CartScreen(
                    paddingValues = paddingValues,
                    onFinish = {
                        saveToCurrentBackStack(navController, "cart", it)
                        navController.navigate(Screen.ReportScreen.route)
                    }
                )
            }
            composable(route = Screen.FavoriteScreen.route) {
                FavoriteScreen (
                    paddingValues = paddingValues,
                    onDetail = {
                        saveToCurrentBackStack(navController, "id", it?.id.toString())
                        navController.navigate(Screen.DetailScreen.route)
                    }
                )
            }
            composable(route = Screen.ProfileScreen.route) {
                ProfileScreen(
                    paddingValues = paddingValues,
                    onVerified = {
                        navController.navigate(Screen.VerifiedScreen.route)
                    },
                    onMaps = {
                        navController.navigate(Screen.MapsScreen.route)
                    },
                    onLogout = {
                        navController.popBackStack()
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.HomeScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = Screen.DetailScreen.route) {
                DetailScreen(
                    paddingValues = paddingValues,
                    productId = getFromPreviousBackStack(navController, "id"),
                    onNavBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = Screen.ReportScreen.route) {
                ReportScreen(
                    paddingValues = paddingValues,
                    product = getFromPreviousBackStack(navController, "cart"),
                    onNavBack = {
                        navController.popBackStack()
                        navController.navigate(Screen.HomeScreen.route)
                    }
                )
            }
            composable(route = Screen.MapsScreen.route) {
                MapsScreen(
                    paddingValues = paddingValues,
                    onNavBack = {
                        navController.popBackStack()
                        navController.navigate(Screen.ProfileScreen.route)
                    }
                )
            }
            composable(route = Screen.VerifiedScreen.route) {
//                VerifiedScreen(
//                    onNavBack = {
//                        navController.popBackStack()
//                        navController.navigate(Screen.ProfileScreen.route)
//                    }
//                )
            }
        }
    }
}
