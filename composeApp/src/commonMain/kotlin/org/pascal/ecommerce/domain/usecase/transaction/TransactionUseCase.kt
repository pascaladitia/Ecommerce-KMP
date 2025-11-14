package org.pascal.ecommerce.domain.usecase.transaction

import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.domain.model.TransactionModel
import org.pascal.ecommerce.utils.base.Result

interface TransactionUseCase {
    suspend fun addTransaction(pref: UserInfo?, products: List<CartEntity>?): Result<Boolean>
    suspend fun getTransactionsByUserId(userId: String): Result<List<TransactionModel>>
    suspend fun addVerified(user: UserInfo): Result<String>
    suspend fun isUserVerified(userId: String): Result<Boolean>
}