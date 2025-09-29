@file:Suppress("unused")
@file:OptIn(ExperimentalObjCName::class)

package org.pascal.ecommerce.utils

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

actual object FirebasePlatform {
    actual fun auth(): FirebaseAuth = Firebase.auth
}

private var pendingCont: CancellableContinuation<String?>? = null

private var googleLauncher: (() -> Unit)? = null

@ObjCName("KMPBridge", exact = true)
object KMPBridge {
    fun registerGoogleLauncher(launcher: () -> Unit) {
        googleLauncher = launcher
    }

    fun onGoogleIdTokenReceived(token: String?) {
        pendingCont?.resume(token)
        pendingCont = null
    }
}

actual object GoogleIdTokenProvider {
    actual suspend fun getIdToken(): String? = suspendCancellableCoroutine { cont ->
        pendingCont?.resume(null)
        pendingCont = cont

        val launch = googleLauncher
        if (launch == null) {
            pendingCont = null
            cont.resume(null)
        } else {
            launch()
        }
    }
}
