package org.pascal.ecommerce.data.local.repository.product

import org.pascal.ecommerce.data.local.entity.ProductEntity

interface ProductLocalRepository {
    suspend fun getProductById(id: Int): ProductEntity?
    suspend fun getAllProduct(): List<ProductEntity>
    suspend fun deleteProductById(item: ProductEntity)
    suspend fun deleteProduct()
    suspend fun insertProduct(item: ProductEntity)
}