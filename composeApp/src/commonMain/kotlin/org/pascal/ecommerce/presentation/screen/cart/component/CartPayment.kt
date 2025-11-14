package org.pascal.ecommerce.presentation.screen.cart.component

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun CartPayment(
    snapUrl: String,
    onFinish: () -> Unit
) {
    val state = rememberWebViewState(url = snapUrl)
    val navigator = rememberWebViewNavigator()

    DisposableEffect(Unit) {
        state.webSettings.isJavaScriptEnabled = true
        onDispose { }
    }

    LaunchedEffect(state.lastLoadedUrl) {
        state.lastLoadedUrl?.let { url ->
            if (url.contains("https://simulator.sandbox.midtrans.com")) {
                onFinish()
            }
        }
    }

    WebView(
        state = state,
        modifier = Modifier.fillMaxSize(),
        navigator = navigator,
        captureBackPresses = true
    )
}