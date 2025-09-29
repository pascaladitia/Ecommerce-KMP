package org.pascal.ecommerce.data.repository.auth

import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.utils.base.AuthResult

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): AuthResult<UserInfo>
    suspend fun signInWithGoogleIdToken(idToken: String, accessToken: String): AuthResult<UserInfo>
    suspend fun signOut(): AuthResult<Unit>
    suspend fun currentUser(): UserInfo?
}