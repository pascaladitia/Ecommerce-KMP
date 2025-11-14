@file:Suppress("unused")
@file:OptIn(ExperimentalObjCName::class)

package org.pascal.ecommerce.utils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import org.pascal.ecommerce.domain.model.GoogleTokens
import kotlin.coroutines.resume
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

actual object FirebasePlatform {
    actual fun auth(): FirebaseAuth = Firebase.auth
}

private var pendingCont: CancellableContinuation<GoogleTokens?>? = null
private var googleLauncher: (() -> Unit)? = null

@ObjCName("KMPBridge", exact = true)
object KMPBridge {
    fun registerGoogleLauncher(launcher: () -> Unit) { googleLauncher = launcher }

    fun onGoogleTokensReceived(idToken: String?, accessToken: String?) {
        val result = if (!idToken.isNullOrBlank() && !accessToken.isNullOrBlank()) {
            GoogleTokens(idToken, accessToken)
        } else null
        pendingCont?.resume(result)
        pendingCont = null
    }
}

actual object GoogleIdTokenProvider {
    actual suspend fun getTokens(): GoogleTokens? = suspendCancellableCoroutine { cont ->
        pendingCont?.resume(null)
        pendingCont = cont
        val launch = googleLauncher
        if (launch == null) { pendingCont = null; cont.resume(null) } else launch()
    }
}
