package org.pascal.ecommerce.data.local.repository.product

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.database.AppDatabase
import org.pascal.ecommerce.data.local.entity.ProductEntity

@Single
class ProductLocalRepository(
    private val database: AppDatabase,
) : ProductLocalRepositoryImpl {

    override suspend fun getProductById(id: Int): ProductEntity? {
        return database.productDao().getProductById(id)
    }

    override suspend fun getAllProduct(): List<ProductEntity> {
        return database.productDao().getAllProducts()
    }

    override suspend fun deleteProductById(item: ProductEntity) {
        return database.productDao().deleteProduct(item)
    }

    override suspend fun deleteProduct() {
        return database.productDao().clearProduct()
    }

    override suspend fun insertProduct(item: ProductEntity) {
        return database.productDao().insertProduct(item)
    }
}