package org.pascal.ecommerce.data.repository.product

import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.remote.dtos.product.ProductResponse

interface ProductRepository {
    suspend fun getProducts(): ProductResponse
    suspend fun getProductByCategory(body: String) : ProductResponse
    suspend fun getProductById(id: Int): ProductEntity
    suspend fun getCategories(): List<String>
}