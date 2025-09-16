package org.pascal.ecommerce.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.pascal.ecommerce.domain.model.BaseProduct
import org.pascal.ecommerce.domain.model.Product

interface ProductUseCaseImpl {
    suspend fun getProduct(): Flow<BaseProduct>
    suspend fun getProductByCategory(body: String): Flow<BaseProduct>
    suspend fun getProductById(id: Int): Flow<Product>
    suspend fun getCategories(): Flow<List<String>>
}