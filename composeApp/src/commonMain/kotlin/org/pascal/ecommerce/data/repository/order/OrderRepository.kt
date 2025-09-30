package org.pascal.ecommerce.data.repository.order

import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse

interface OrderRepository {
    suspend fun createSnapTransaction(amountInUSD: Double): TransactionResponse
}