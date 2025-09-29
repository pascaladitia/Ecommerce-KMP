package org.pascal.ecommerce.utils

import dev.gitlive.firebase.auth.FirebaseAuth
import org.pascal.ecommerce.domain.model.GoogleTokens

expect object FirebasePlatform {
    fun auth(): FirebaseAuth
}

expect object GoogleIdTokenProvider {
    suspend fun getTokens(): GoogleTokens?
}