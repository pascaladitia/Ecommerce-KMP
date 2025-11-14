package org.pascal.ecommerce.data.repository.product

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.remote.api.ProductApi
import org.pascal.ecommerce.data.remote.dtos.product.ProductResponse

@Single
class ProductRepositoryImpl : ProductRepository {
    override suspend fun getProducts(): ProductResponse {
        return ProductApi.getProduct()
    }

    override suspend fun getProductByCategory(body: String): ProductResponse {
        return ProductApi.getProductByCategory(body)
    }

    override suspend fun getProductById(id: Int): ProductEntity {
        return ProductApi.getProductById(id)
    }

    override suspend fun getCategories(): List<String> {
        return ProductApi.getCategories()
    }
}