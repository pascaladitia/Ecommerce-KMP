package org.pascal.ecommerce.utils.base

import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class DataNotFoundException(message: String = "Data not found") : Exception(message)
class InternalServerError(
    message: String = "Maaf, sistem sedang tidak dapat diakses. Silakan coba beberapa saat lagi"
) : Exception(message)

open class KtorHttpException(
    val httpResponse: HttpResponse,
    message: String = "HTTP ${httpResponse.status}"
) : ResponseException(httpResponse, message)

class BadRequestError(
    response: HttpResponse,
    message: String = "Request error"
) : KtorHttpException(response, message)

val DefaultJson = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
}

fun HttpStatusCode.isSuccess(): Boolean = value in 200..299

suspend inline fun <reified T> HttpResponse.handleApi(): T {
    return when {
        status.isSuccess() -> body()
        status.value == 400 -> throw BadRequestError(this)
        status.value == 404 -> throw DataNotFoundException()
        status.value in 500..599 -> throw InternalServerError()
        else -> throw KtorHttpException(this)
    }
}

suspend inline fun <reified T> ResponseException.getErrorBody(json: Json = DefaultJson): T? {
    return runCatching {
        val text = response.bodyAsText()
        json.decodeFromString<T>(text)
    }.getOrElse {
        it.printStackTrace()
        null
    }
}

suspend inline fun <reified T> BadRequestError.getErrorBody(json: Json = DefaultJson): T? {
    return (this as ResponseException).getErrorBody(json)
}
