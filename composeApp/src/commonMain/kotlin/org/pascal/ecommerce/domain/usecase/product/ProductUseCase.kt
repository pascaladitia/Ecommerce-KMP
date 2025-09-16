package org.pascal.ecommerce.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.repository.ProductRepository
import org.pascal.ecommerce.domain.mapper.toDomain
import org.pascal.ecommerce.domain.model.BaseProduct
import org.pascal.ecommerce.domain.model.Product

@Single
class ProductUseCase(
    val repository: ProductRepository
) : ProductUseCaseImpl {

    override suspend fun getProduct(): Flow<BaseProduct> = flow {
        emit(repository.getProducts().toDomain())
    }

    override suspend fun getProductByCategory(body: String): Flow<BaseProduct> = flow {
        emit(repository.getProductByCategory(body).toDomain())
    }

    override suspend fun getProductById(id: Int): Flow<Product> = flow {
        emit(repository.getProductById(id).toDomain())
    }

    override suspend fun getCategories(): Flow<List<String>> = flow {
        emit(repository.getCategories())
    }
}