package org.pascal.ecommerce.data.remote

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single
import org.pascal.ecommerce.BuildKonfig
import org.pascal.ecommerce.data.remote.dtos.BaseResponse
import org.pascal.ecommerce.utils.base.handleApi

@Single
object KtorClientApi {
    suspend fun postApi(body: String): BaseResponse {
        return clientKtorApi.post("${BuildKonfig.BASE_URL}/create") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.handleApi()
    }
}