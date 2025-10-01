package org.pascal.ecommerce.data.repository.auth

import dev.gitlive.firebase.auth.GoogleAuthProvider
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.utils.FirebasePlatform
import org.pascal.ecommerce.utils.base.Result

class AuthRepositoryImpl : AuthRepository {

    private val auth = FirebasePlatform.auth()

    override suspend fun signInWithEmail(email: String, password: String): Result<UserInfo> = try {
        auth.signInWithEmailAndPassword(email, password)
        Result.Success(currentUser()!!)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<UserInfo> = try {
        auth.createUserWithEmailAndPassword(email, password)
        Result.Success(currentUser()!!)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    override suspend fun signInWithGoogleIdToken(idToken: String, accessToken: String): Result<UserInfo> = try {
        val credential = GoogleAuthProvider.credential(idToken, accessToken)
        auth.signInWithCredential(credential)
        Result.Success(currentUser()!!)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    override suspend fun signOut(): Result<Unit> = try {
        auth.signOut()
        Result.Success(Unit)
    } catch (t: Throwable) {
        Result.Error(t)
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
