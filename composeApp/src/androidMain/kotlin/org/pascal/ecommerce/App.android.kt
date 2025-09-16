package org.pascal.ecommerce

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.pascal.ecommerce.data.local.AppDatabase
import org.pascal.ecommerce.di.initKoin

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App() }

        ContextUtils.setContext(context = this)
        initKoin {
            androidLogger(level = Level.NONE)
            androidContext(androidContext = this@AppActivity)
        }
    }
}

actual fun createSettings(): Settings {
    val context: Context = ContextUtils.context
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return SharedPreferencesSettings(preferences)
}

actual fun getDatabaseBuilder(): AppDatabase {
    val context: Context = ContextUtils.context
    val dbFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@SuppressLint("ContextCastToActivity")
@Composable
actual fun PlatformColors(statusBarColor: Color, navBarColor: Color){
    val activity = LocalContext.current as ComponentActivity
    val window = activity.window

    window.statusBarColor = statusBarColor.value.toInt()
}