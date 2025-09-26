package org.pascal.ecommerce.data.repository.auth

import dev.gitlive.firebase.auth.GoogleAuthProvider
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.utils.FirebasePlatform
import org.pascal.ecommerce.utils.base.AuthResult

class AuthRepositoryImpl : AuthRepository {

    private val auth = FirebasePlatform.auth()

    override suspend fun signInWithEmail(email: String, password: String): AuthResult<UserInfo> = try {
        auth.signInWithEmailAndPassword(email, password)
        AuthResult.Success(currentUser()!!)
    } catch (t: Throwable) {
        AuthResult.Error(t)
    }

    override suspend fun signInWithGoogleIdToken(idToken: String): AuthResult<UserInfo> = try {
        val credential = GoogleAuthProvider.credential(idToken, null)
        auth.signInWithCredential(credential)
        AuthResult.Success(currentUser()!!)
    } catch (t: Throwable) {
        AuthResult.Error(t)
    }

    override suspend fun signOut(): AuthResult<Unit> = try {
        auth.signOut()
        AuthResult.Success(Unit)
    } catch (t: Throwable) {
        AuthResult.Error(t)
    }

    override suspend fun currentUser(): UserInfo? {
        val u = auth.currentUser
        return u?.let {
            UserInfo(
                uid = it.uid,
                email = it.email,
                displayName = it.displayName ?: "",
                photoUrl = it.photoURL ?: ""
            )
        }
    }
}
