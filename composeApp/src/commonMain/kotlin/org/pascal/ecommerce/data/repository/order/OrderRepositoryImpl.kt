package org.pascal.ecommerce.data.repository.order

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.remote.api.TransactionApi
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse

@Single
class OrderRepositoryImpl : OrderRepository {
    override suspend fun createSnapTransaction(amountInUSD: Double): TransactionResponse {
        return TransactionApi.createSnapTransaction(amountInUSD)
    }
}