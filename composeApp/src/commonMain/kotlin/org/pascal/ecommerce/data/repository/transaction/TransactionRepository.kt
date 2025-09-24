package org.pascal.ecommerce.data.repository.transaction

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.remote.api.TransactionApi
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse

@Single
class TransactionRepository : TransactionRepositoryImpl {
    override suspend fun createSnapTransaction(amountInUSD: Double): TransactionResponse {
        return TransactionApi.createSnapTransaction(amountInUSD)
    }
}