package org.pascal.ecommerce.domain.usecase.auth

import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.data.repository.auth.AuthRepository
import org.pascal.ecommerce.utils.base.Result

class AuthUseCaseImpl(
    private val repo: AuthRepository
): AuthUseCase {
    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<UserInfo> {
        return repo.signInWithEmail(email, password)
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<UserInfo> {
        return repo.signUpWithEmail(email, password)
    }

    override suspend fun signInWithGoogleIdToken(idToken: String, accessToken: String): Result<UserInfo> {
        return repo.signInWithGoogleIdToken(idToken, accessToken)
    }

    override suspend fun signOut(): Result<Unit> {
        return repo.signOut()
    }

    override suspend fun currentUser(): UserInfo? = repo.currentUser()
}