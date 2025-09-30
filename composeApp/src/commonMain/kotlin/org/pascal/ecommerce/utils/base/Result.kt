package org.pascal.ecommerce.utils.base

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val throwable: Throwable? = null): Result<Nothing>()
}