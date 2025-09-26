@file:Suppress("unused")

package org.pascal.ecommerce.utils


import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIViewController
import kotlin.coroutines.resume

actual object FirebasePlatform {
    actual fun auth(): FirebaseAuth = Firebase.auth
}

private var rootProvider: (() -> UIViewController)? = null
private object IosGoogleConfig {
    var iosClientId: String? = null
    var webClientId: String? = null
}

fun provideRootViewController(provider: () -> UIViewController) {
    rootProvider = provider
}

fun provideGoogleClientIds(iosClientId: String, webClientId: String?) {
    IosGoogleConfig.iosClientId = iosClientId
    IosGoogleConfig.webClientId = webClientId
}

@OptIn(ExperimentalForeignApi::class)
actual object GoogleIdTokenProvider {
    actual suspend fun getIdToken(): String? = suspendCancellableCoroutine { cont ->
//        val presenter = rootProvider?.invoke()
//        val iosId = IosGoogleConfig.iosClientId
//        val webId = IosGoogleConfig.webClientId
//
//        if (presenter == null || iosId.isNullOrBlank()) {
//            cont.resume(null)
//            return@suspendCancellableCoroutine
//        }
//
//        val config = if (webId.isNullOrBlank()) {
//            GIDConfiguration(clientID = iosId)
//        } else {
//            GIDConfiguration(clientID = iosId, serverClientID = webId)
//        }
//
//        GIDSignIn.sharedInstance.signInWithPresentingViewController(
//            presentingViewController = presenter,
//            configuration = config
//        ) { result, error ->
//            if (error != null || result == null) {
//                cont.resume(null)
//            } else {
//                cont.resume(result.user?.idToken?.tokenString)
//            }
//        }
    }
}
