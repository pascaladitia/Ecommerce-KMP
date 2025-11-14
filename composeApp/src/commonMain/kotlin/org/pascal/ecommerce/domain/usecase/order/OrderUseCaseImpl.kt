package org.pascal.ecommerce.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.repository.order.OrderRepositoryImpl
import org.pascal.ecommerce.domain.mapper.toDomain
import org.pascal.ecommerce.domain.model.Order

@Single
class OrderUseCaseImpl(
    val repository: OrderRepositoryImpl
) : OrderUseCase {

    override suspend fun createSnapTransaction(amountInUSD: Double): Flow<Order> = flow {
        emit(repository.createSnapTransaction(amountInUSD).toDomain())
    }

}