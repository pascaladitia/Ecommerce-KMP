package org.pascal.ecommerce.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.repository.transaction.TransactionRepository
import org.pascal.ecommerce.domain.mapper.toDomain
import org.pascal.ecommerce.domain.model.Transaction

@Single
class TransactionUseCase(
    val repository: TransactionRepository
) : TransactionUseCaseImpl {

    override suspend fun createSnapTransaction(amountInUSD: Double): Flow<Transaction> = flow {
        emit(repository.createSnapTransaction(amountInUSD).toDomain())
    }

}