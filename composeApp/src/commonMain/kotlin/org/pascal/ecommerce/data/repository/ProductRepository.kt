package org.pascal.ecommerce.data.repository

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.remote.KtorClientApi
import org.pascal.ecommerce.data.remote.dtos.ProductResponse

@Single
class ProductRepository : ProductRepositoryImpl {
    override suspend fun getProducts(): ProductResponse {
        return KtorClientApi.getProduct()
    }

    override suspend fun getProductByCategory(body: String): ProductResponse {
        return KtorClientApi.getProductByCategory(body)
    }

    override suspend fun getProductById(id: Int): ProductEntity {
        return KtorClientApi.getProductById(id)
    }

    override suspend fun getCategories(): List<String> {
        return KtorClientApi.getCategories()
    }
}