package org.pascal.ecommerce.domain.usecase.transaction

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.data.repository.transaction.TransactionRepository
import org.pascal.ecommerce.domain.model.TransactionModel
import org.pascal.ecommerce.utils.base.Result

@Single
class TransactionUseCaseImpl(
    val repository: TransactionRepository
) : TransactionUseCase {

    override suspend fun addTransaction(
        pref: UserInfo?,
        products: List<CartEntity>?
    ): Result<Boolean> {
        return repository.addTransaction(pref, products)
    }

    override suspend fun getTransactionsByUserId(userId: String): Result<List<TransactionModel>> {
        return repository.getTransactionsByUserId(userId)
    }

    override suspend fun addVerified(user: UserInfo): Result<String> {
        return repository.addVerified(user)
    }

    override suspend fun isUserVerified(userId: String): Result<Boolean> {
        return repository.isUserVerified(userId)
    }

}