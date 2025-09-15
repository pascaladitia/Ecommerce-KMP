package org.pascal.ecommerce.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.ic_logo
import ecommerce_kmp.composeapp.generated.resources.no_profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(16.dp)
            .background(Color.White)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val navigationItems = listOf(
                NavigationItem(
                    title = "Beranda",
                    icon = Res.drawable.ic_logo,
                    screen = Screen.HomeScreen
                ),
                NavigationItem(
                    title = "Panduan",
                    icon = Res.drawable.ic_logo,
                    screen = Screen.GuideScreen
                ),
                NavigationItem(
                    title = "Riwayat",
                    icon = Res.drawable.ic_logo,
                    screen = Screen.HistoryScreen
                ),
                NavigationItem(
                    title = "Profile",
                    icon = Res.drawable.ic_logo,
                    screen = Screen.ProfileScreen
                )
            )
            navigationItems.map { item ->
                NavigationBarItem(
                    icon = {
                        if (item.screen == Screen.ProfileScreen) {
                            Image(
                                painter = painterResource(Res.drawable.no_profile),
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp),
                                tint = if (currentRoute == item.screen.route) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (currentRoute == item.screen.route) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    alwaysShowLabel = true,
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
