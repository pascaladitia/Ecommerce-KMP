package org.pascal.ecommerce.data.repository.auth

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.GoogleAuthProvider
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.utils.base.AuthResult

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
): AuthRepository {
    override suspend fun signInWithEmail(email: String, password: String): AuthResult<UserInfo> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password)
        val user = result.user ?: error("User is null after signIn")
        user.toUserInfo().let { AuthResult.Success(it) }
    }.getOrElse { AuthResult.Error(it.message ?: "Login gagal", it) }


    override suspend fun signInWithGoogleIdToken(idToken: String): AuthResult<UserInfo> = runCatching {
        val credential = GoogleAuthProvider.credential(idToken = idToken, accessToken = null)
        val result = auth.signInWithCredential(credential)
        val user = result.user ?: error("User is null after Google signIn")
        user.toUserInfo().let { AuthResult.Success(it) }
    }.getOrElse { AuthResult.Error(it.message ?: "Login Google gagal", it) }


    override suspend fun signOut(): AuthResult<Unit> = runCatching {
        auth.signOut()
        AuthResult.Success(Unit)
    }.getOrElse { AuthResult.Error(it.message ?: "Gagal logout", it) }


    override suspend fun currentUser(): UserInfo? = auth.currentUser?.toUserInfo()
}

private fun dev.gitlive.firebase.auth.FirebaseUser.toUserInfo() = UserInfo(
    uid = uid,
    email = email,
    displayName = displayName,
    photoUrl = photoURL
)