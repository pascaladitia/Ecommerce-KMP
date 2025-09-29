package org.pascal.ecommerce.domain.usecase.auth

import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.data.repository.auth.AuthRepository
import org.pascal.ecommerce.utils.base.AuthResult

class AuthUseCaseImpl(
    private val repo: AuthRepository
): AuthUseCase {
    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): AuthResult<UserInfo> {
        return repo.signInWithEmail(email, password)
    }

    override suspend fun signInWithGoogleIdToken(idToken: String, accessToken: String): AuthResult<UserInfo> {
        return repo.signInWithGoogleIdToken(idToken, accessToken)
    }

    override suspend fun signOut(): AuthResult<Unit> {
        return repo.signOut()
    }

    override suspend fun currentUser(): UserInfo? = repo.currentUser()
}