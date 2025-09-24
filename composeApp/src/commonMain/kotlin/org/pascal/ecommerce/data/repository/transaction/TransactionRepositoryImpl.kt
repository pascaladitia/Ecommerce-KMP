package org.pascal.ecommerce.data.repository.transaction

import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse

interface TransactionRepositoryImpl {
    suspend fun createSnapTransaction(amountInUSD: Double): TransactionResponse
}