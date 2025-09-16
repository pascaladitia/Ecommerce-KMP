package org.pascal.ecommerce

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.russhwolf.settings.Settings
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.pascal.ecommerce.data.local.database.AppDatabase
import org.pascal.ecommerce.presentation.navigation.RouteScreen
import org.pascal.ecommerce.theme.AppTheme

@Composable
@Preview
fun App() = AppTheme {
    RouteScreen()
}

expect fun createSettings(): Settings

expect fun getDatabaseBuilder(): AppDatabase

@Composable
expect fun PlatformColors(statusBarColor: Color, navBarColor: Color)
