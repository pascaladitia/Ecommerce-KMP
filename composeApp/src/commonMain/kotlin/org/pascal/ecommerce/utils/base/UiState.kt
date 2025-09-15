package org.pascal.ecommerce.utils.base

import androidx.compose.runtime.Composable
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException

const val NO_INTERNET_CONN_ERROR = "Maaf, sistem sedang tidak dapat diakses. Silakan coba beberapa saat lagi."
const val UNKNOWN_ERROR = "Maaf, sistem sedang tidak dapat diakses. Silakan coba beberapa saat lagi."

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Default<T> : UiState<T>()
    class Empty<T> : UiState<T>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure<T>(val throwable: Throwable?, val message: String?) : UiState<T>()

    companion object {
        fun <T> loading(): UiState<T> = Loading()
        fun <T> default(): UiState<T> = Default()
        fun <T> success(data: T): UiState<T> = Success(data)
        fun <T> empty(): UiState<T> = Empty()

        fun <T> fail(throwable: Throwable, message: String?): UiState<T> {
            return if (throwable.isNetworkIssue()) {
                Failure(throwable, NO_INTERNET_CONN_ERROR)
            } else {
                val fallback = message?.maskHttpErrorCodes()
                    ?: throwable.cause?.message?.maskHttpErrorCodes()
                    ?: throwable.message?.maskHttpErrorCodes()
                    ?: UNKNOWN_ERROR
                Failure(throwable, fallback)
            }
        }

        fun <T> fail(message: String?): UiState<T> {
            return Failure(null, message?.maskHttpErrorCodes() ?: UNKNOWN_ERROR)
        }

        fun <T> UiState<T>.getSuccessData(): T? = (this as? Success)?.data

        fun <T> UiState<T>.getErrorMessage(): String =
            (this as? Failure)?.let { f ->
                f.message?.maskHttpErrorCodes()
                    ?: f.throwable?.message?.maskHttpErrorCodes()
                    ?: UNKNOWN_ERROR
            } ?: ""

        @Composable
        fun <T> UiState<T>.checkState(
            onDefault: @Composable () -> Unit = { },
            onLoading: @Composable () -> Unit = { },
            onEmpty: @Composable () -> Unit = { },
            onSuccess: @Composable (T) -> Unit = { },
            onFailure: @Composable (Throwable?, String) -> Unit = { _, _ -> }
        ): UiState<T> {
            when (this) {
                is Default -> onDefault()
                is Loading -> onLoading()
                is Empty -> onEmpty()
                is Success -> onSuccess(this.data)
                is Failure -> onFailure(this.throwable, getErrorMessage())
            }
            return this
        }

        fun <T> UiState<T>.checkStateNonComposable(
            onDefault: () -> Unit = { },
            onLoading: () -> Unit = { },
            onEmpty: () -> Unit = { },
            onSuccess: (T) -> Unit = { },
            onFailure: (Throwable?, String) -> Unit = { _, _ -> }
        ): UiState<T> {
            when (this) {
                is Default -> onDefault()
                is Loading -> onLoading()
                is Empty -> onEmpty()
                is Success -> onSuccess(this.data)
                is Failure -> onFailure(this.throwable, getErrorMessage())
            }
            return this
        }

        fun <T> UiState<T>.doOnDefault(action: () -> Unit) {
            if (this is Default) action()
        }
    }
}

private fun Throwable.isNetworkIssue(): Boolean =
    this is UnresolvedAddressException ||
            this is ConnectTimeoutException ||
            this is SocketTimeoutException ||
            this is HttpRequestTimeoutException ||
            this is IOException

fun String.maskHttpErrorCodes(): String {
    val maskSSLPinningFailure = "Terjadi kesalahan pada sertifikat sistem"
    val mask4xxMessage = "Terjadi kesalahan pada sistem"
    val mask5xxMessage = "Sistem sedang bermasalah"

    val errorCode4xxRegex = Regex("\\b4\\d{2}\\b")
    val errorCode5xxRegex = Regex("\\b5\\d{2}\\b")

    if (this.contains("pinning failure", ignoreCase = true)) return maskSSLPinningFailure
    if (errorCode5xxRegex.containsMatchIn(this)) return mask5xxMessage
    if (errorCode4xxRegex.containsMatchIn(this)) return mask4xxMessage

    return this
}
