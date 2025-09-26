package org.pascal.ecommerce.utils

import dev.gitlive.firebase.auth.FirebaseAuth

expect object FirebasePlatform {
    fun auth(): FirebaseAuth
}

expect object GoogleIdTokenProvider {
    suspend fun getIdToken(): String?
}