package org.pascal.ecommerce.utils.base

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import org.pascal.ecommerce.utils.base.EventAction.Companion.getErrorMessage

sealed class EventAction<T> {
    class Loading<T> : EventAction<T>()
    class Empty<T> : EventAction<T>()
    data class Success<T>(val data: T) : EventAction<T>()
    data class Failure<T>(val throwable: Throwable?, val message: String?) : EventAction<T>()

    companion object {

        fun <T> loading(): EventAction<T> = Loading()
        fun <T> success(data: T): EventAction<T> = Success(data)
        fun <T> empty(): EventAction<T> = Empty()
        fun <T> fail(message: String?): EventAction<T> = Failure(null, message)
        fun <T> fail(throwable: Throwable, message: String?): EventAction<T> {
            val fallbackMsg = message?.maskHttpErrorCodes()
                ?: throwable.cause?.message?.maskHttpErrorCodes()
                ?: UNKNOWN_ERROR

            return if (throwable.isNetworkIssue()) {
                Failure(throwable, NO_INTERNET_CONN_ERROR)
            } else {
                Failure(throwable, fallbackMsg)
            }
        }

        fun <T> EventAction<T>.getSuccessData(): T? = (this as? Success)?.data

        fun <T> EventAction<T>.getErrorMessage(): String {
            return (this as? Failure)?.let { f ->
                f.message?.maskHttpErrorCodes()
                    ?: f.throwable?.message?.maskHttpErrorCodes()
                    ?: UNKNOWN_ERROR
            } ?: ""
        }
    }
}

private fun Throwable.isNetworkIssue(): Boolean =
    this is UnresolvedAddressException ||
            this is ConnectTimeoutException ||
            this is SocketTimeoutException ||
            this is HttpRequestTimeoutException ||
            this is IOException

suspend fun <T> Channel<EventAction<T>>.checkChannelValue(
    onLoading: () -> Unit = { },
    onEmpty: () -> Unit = { },
    onSuccess: (T) -> Unit = { },
    onFailure: (Throwable?, String) -> Unit = { _, _ -> }
) {
    for (event in this) {
        when (event) {
            is EventAction.Loading -> onLoading()
            is EventAction.Empty -> onEmpty()
            is EventAction.Success -> onSuccess(event.data)
            is EventAction.Failure -> onFailure(event.throwable, event.getErrorMessage())
        }
    }
}

suspend fun <T> Channel<EventAction<T>>.sendLoading() {
    send(EventAction.loading())
}

suspend fun <T> Channel<EventAction<T>>.sendFailure(message: String?) {
    send(EventAction.fail(message))
}

suspend fun <T> Channel<EventAction<T>>.sendFailure(ex: Throwable, message: String?) {
    send(EventAction.fail(ex, message))
}

suspend fun <T> Channel<EventAction<T>>.sendSuccess(data: T) {
    send(EventAction.success(data))
}

suspend fun <T> Channel<EventAction<T>>.sendEmpty() {
    send(EventAction.empty())
}

suspend fun <T> SharedFlow<EventAction<T>>.checkChannelValue(
    onLoading: () -> Unit = { },
    onEmpty: () -> Unit = { },
    onSuccess: (T) -> Unit = { },
    onFailure: (Throwable?, String) -> Unit = { _, _ -> }
) {
    collect { event ->
        when (event) {
            is EventAction.Loading -> onLoading()
            is EventAction.Empty -> onEmpty()
            is EventAction.Success -> onSuccess(event.data)
            is EventAction.Failure -> onFailure(event.throwable, event.getErrorMessage())
        }
    }
}

suspend fun <T> MutableSharedFlow<EventAction<T>>.sendLoading() {
    emit(EventAction.loading())
}

suspend fun <T> MutableSharedFlow<EventAction<T>>.sendFailure(message: String?) {
    emit(EventAction.fail(message))
}

suspend fun <T> MutableSharedFlow<EventAction<T>>.sendFailure(ex: Throwable, message: String?) {
    emit(EventAction.fail(ex, message))
}

suspend fun <T> MutableSharedFlow<EventAction<T>>.sendSuccess(data: T) {
    emit(EventAction.success(data))
}

suspend fun <T> MutableSharedFlow<EventAction<T>>.sendEmpty() {
    emit(EventAction.empty())
}
