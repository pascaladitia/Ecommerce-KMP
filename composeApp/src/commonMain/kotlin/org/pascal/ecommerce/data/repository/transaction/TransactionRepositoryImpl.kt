package org.pascal.ecommerce.data.repository.transaction

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.remote.api.TransactionApi
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse

@Single
class TransactionRepositoryImpl : TransactionRepository {
    override suspend fun createSnapTransaction(amountInUSD: Double): TransactionResponse {
        return TransactionApi.createSnapTransaction(amountInUSD)
    }
}