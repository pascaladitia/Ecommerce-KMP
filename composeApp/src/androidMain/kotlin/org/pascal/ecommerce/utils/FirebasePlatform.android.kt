// composeApp/src/androidMain/kotlin/org/pascal/ecommerce/utils/FirebasePlatform.android.kt
package org.pascal.ecommerce.utils

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.pascal.ecommerce.domain.model.GoogleTokens
import kotlin.coroutines.resume

object ActivityProvider {
    lateinit var get: () -> Activity
}

actual object FirebasePlatform {
    actual fun auth(): FirebaseAuth = Firebase.auth
}

actual object GoogleIdTokenProvider {
    actual suspend fun getTokens(): GoogleTokens? {
        val activity = ActivityProvider.get()
        val component = activity as? ComponentActivity
            ?: error("Activity must be a ComponentActivity")

        val serverClientId = activity.getString(org.pascal.ecommerce.R.string.default_web_client_id)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .requestScopes(Scope(Scopes.EMAIL), Scope(Scopes.PROFILE), Scope("openid"))
            .build()

        val client = GoogleSignIn.getClient(activity, gso)

        GoogleSignIn.getLastSignedInAccount(activity)?.let { acc ->
            val idToken = acc.idToken ?: run {
                runCatching { client.silentSignIn().await()?.idToken }.getOrNull()
            }
            if (!idToken.isNullOrBlank()) {
                val access = fetchAccessToken(activity, acc)
                if (!access.isNullOrBlank()) return GoogleTokens(idToken, access)
            }
        }

        val silent = runCatching { client.silentSignIn().await() }.getOrNull()
        if (silent != null) {
            val idToken = silent.idToken
            val access = fetchAccessToken(activity, silent)
            if (!idToken.isNullOrBlank() && !access.isNullOrBlank()) {
                return GoogleTokens(idToken, access)
            }
        }

        val resultIntent = startForResultAwait(component, client.signInIntent) ?: return null
        val task = GoogleSignIn.getSignedInAccountFromIntent(resultIntent)
        val account = try { task.await() } catch (_: ApiException) { null } ?: return null

        val idToken = account.idToken ?: return null
        val access = fetchAccessToken(activity, account) ?: return null

        return GoogleTokens(idToken, access)
    }
}

private suspend fun fetchAccessToken(activity: Activity, account: com.google.android.gms.auth.api.signin.GoogleSignInAccount): String? {
    val acct = account.account ?: return null
    val scope = "oauth2:email profile openid"
    return withContext(Dispatchers.IO) {
        try {
            GoogleAuthUtil.getToken(activity.applicationContext, acct, scope)
        } catch (e: UserRecoverableAuthException) {
            val data = startForResultAwait(activity as ComponentActivity, e.intent!!)
            if (data == null) null
            else runCatching { GoogleAuthUtil.getToken(activity.applicationContext, acct, scope) }.getOrNull()
        } catch (_: Throwable) {
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
