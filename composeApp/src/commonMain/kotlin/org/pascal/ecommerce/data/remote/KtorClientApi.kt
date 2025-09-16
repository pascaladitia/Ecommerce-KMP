package org.pascal.ecommerce.data.remote

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single
import org.pascal.ecommerce.BuildKonfig
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.remote.dtos.BaseResponse
import org.pascal.ecommerce.data.remote.dtos.ProductResponse
import org.pascal.ecommerce.utils.base.handleApi

@Single
object KtorClientApi {
    suspend fun getProduct(): ProductResponse {
        return client.get("${BuildKonfig.BASE_URL}/products").handleApi()
    }

    suspend fun getProductByCategory(body: String): ProductResponse {
        return client.post("${BuildKonfig.BASE_URL}/products/category/$body").handleApi()
    }

    suspend fun getProductById(id: Int): ProductEntity {
        return client.get("${BuildKonfig.BASE_URL}/products/$id").handleApi()
    }

    suspend fun getCategories(): List<String> {
        return client.get("${BuildKonfig.BASE_URL}/products/category-list").handleApi()
    }
}