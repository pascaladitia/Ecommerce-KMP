package org.pascal.ecommerce.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single
import org.pascal.ecommerce.BuildKonfig
import org.pascal.ecommerce.domain.model.BaseResponse

@Single
object KtorClientApi {
    suspend fun createDetector(body: String): BaseResponse {
        return clientKtorApi.post("${BuildKonfig.BASE_URL}/create-conversation") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }
}