package org.pascal.ecommerce.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.pascal.ecommerce.domain.model.Transaction

interface TransactionUseCase {
    suspend fun createSnapTransaction(amountInUSD: Double): Flow<Transaction>
}