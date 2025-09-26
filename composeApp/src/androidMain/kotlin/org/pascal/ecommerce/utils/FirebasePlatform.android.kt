package org.pascal.ecommerce.utils

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

object ActivityProvider {
    lateinit var get: () -> Activity
}

actual object FirebasePlatform {
    actual fun auth(): FirebaseAuth = Firebase.auth
}

actual object GoogleIdTokenProvider {
    actual suspend fun getIdToken(): String? {
        val activity = ActivityProvider.get()
        val component = activity as? ComponentActivity
            ?: error("Activity must be a ComponentActivity")

        val serverClientId = "AIzaSyBghHp2MewwZ_76xgqWBS7pNr6amon_LWw"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(activity, gso)

        GoogleSignIn.getLastSignedInAccount(activity)?.idToken?.let { return it }
        runCatching { client.silentSignIn().await()?.idToken }.getOrNull()?.let { return it }

        val intent = client.signInIntent
        val resultIntent = startForResultAwait(component, intent) ?: return null
        val task = GoogleSignIn.getSignedInAccountFromIntent(resultIntent)
        return try {
            task.await().idToken
        } catch (_: ApiException) {
            null
        }
    }
}

private suspend fun startForResultAwait(
    activity: ComponentActivity,
    intent: Intent
): Intent? = suspendCancellableCoroutine { cont ->
    val key = "google_sign_in_${SystemClock.uptimeMillis()}"

    var launcher: androidx.activity.result.ActivityResultLauncher<Intent>? = null
    var resumed = false

    launcher = activity.activityResultRegistry.register(
        key,
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (!resumed) {
            resumed = true
            val data = res.data
            try { launcher?.unregister() } catch (_: Throwable) {}
            cont.resume(data)
        }
    }

    cont.invokeOnCancellation {
        try { launcher?.unregister() } catch (_: Throwable) {}
        resumed = true
    }

    launcher.launch(intent)
}


