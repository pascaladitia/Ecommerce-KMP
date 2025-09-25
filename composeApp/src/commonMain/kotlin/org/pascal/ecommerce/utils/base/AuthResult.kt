package org.pascal.ecommerce.utils.base

sealed class AuthResult<out T> {
    data class Success<T>(val data: T): AuthResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null): AuthResult<Nothing>()
}