package org.pascal.ecommerce

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import org.pascal.ecommerce.data.local.database.AppDatabase
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UINavigationBar
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIWindow

actual fun createSettings(): Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}

actual fun getDatabaseBuilder(): AppDatabase {
    val dbFile = "${NSHomeDirectory()}/app.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile,
        factory = { AppDatabase::class.instantiateImpl() }
    ).setDriver(_root_ide_package_.androidx.sqlite.driver.bundled.BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@Composable
actual fun PlatformColors(
    statusBarColor: Color,
    navBarColor: Color
) {
    val statusBar = statusBarView()
    SideEffect {
        statusBar.backgroundColor = statusBarColor.toUIColor()
        UINavigationBar.appearance().backgroundColor = navBarColor.toUIColor()
    }
}


@OptIn(ExperimentalForeignApi::class)
@Composable
private fun statusBarView() = remember {
    val keyWindow: UIWindow? =
        UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true } as? UIWindow
    val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
    val width = UIScreen.mainScreen.bounds.useContents { this.size.width }
    var topInsets = 0.0

    safeAreaInsets?.let {
        topInsets = safeAreaInsets.useContents {
            this.top
        }
    }
    val tag = 3848245L

    val statusBarView = UIView(frame = CGRectMake(0.0,0.0, width, topInsets))

    keyWindow?.viewWithTag(tag) ?: run {
        statusBarView.tag = tag
        statusBarView.layer.zPosition = 999999.0
        keyWindow?.addSubview(statusBarView)
        statusBarView
    }
}

private fun Color.toUIColor(): UIColor = UIColor(
    red = this.red.toDouble(),
    green = this.green.toDouble(),
    blue = this.blue.toDouble(),
    alpha = this.alpha.toDouble()
)