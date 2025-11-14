package org.pascal.ecommerce.domain.usecase.auth

import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.utils.base.Result

interface AuthUseCase {
    suspend fun signInWithEmail(email: String, password: String): Result<UserInfo>
    suspend fun signUpWithEmail(email: String, password: String): Result<UserInfo>
    suspend fun signInWithGoogleIdToken(idToken: String, accessToken: String): Result<UserInfo>
    suspend fun signOut(): Result<Unit>
    suspend fun currentUser(): UserInfo?
}