package org.pascal.ecommerce.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import org.pascal.ecommerce.domain.model.Order

interface OrderUseCase {
    suspend fun createSnapTransaction(amountInUSD: Double): Flow<Order>
}