package org.pascal.ecommerce.data.remote.api

import io.ktor.client.request.get
import io.ktor.client.request.post
import org.koin.core.annotation.Single
import org.pascal.ecommerce.BuildKonfig
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.remote.client
import org.pascal.ecommerce.data.remote.dtos.product.ProductResponse
import org.pascal.ecommerce.utils.base.handleApi

@Single
object ProductApi {
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