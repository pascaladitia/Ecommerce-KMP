package org.pascal.ecommerce.utils.base

sealed class AuthResult<out T> {
    data class Success<T>(val data: T): AuthResult<T>()
    data class Error(val throwable: Throwable? = null): AuthResult<Nothing>()
}