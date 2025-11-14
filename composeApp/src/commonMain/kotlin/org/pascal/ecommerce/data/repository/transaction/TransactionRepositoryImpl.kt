package org.pascal.ecommerce.data.repository.transaction

import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.where
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.domain.model.TransactionModel
import org.pascal.ecommerce.utils.base.Result
import org.pascal.ecommerce.utils.getCurrentDate

@Single
class TransactionRepositoryImpl(
    private val firestore: FirebaseFirestore
) : TransactionRepository {

    override suspend fun addTransaction(
        pref: UserInfo?,
        products: List<CartEntity>?
    ): Result<Boolean> = try {
        val tx = TransactionModel(
            userId = pref?.uid,
            userName = pref?.displayName,
            date = getCurrentDate(),
            total = calculateTotalPrice(products ?: emptyList()),
            products = products ?: emptyList()
        )

        firestore.collection("transaction")
            .add(tx)
        Result.Success(true)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    override suspend fun getTransactionsByUserId(
        userId: String
    ): Result<List<TransactionModel>> = try {
        val snap = firestore.collection("transaction")
            .where("userId", equalTo = userId)
            .get()

        val list = snap.documents.map { it.data<TransactionModel>() }
        Result.Success(list)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    override suspend fun addVerified(user: UserInfo): Result<String> = try {
        val id = user.uid ?: return Result.Error(IllegalArgumentException("user.id is null"))
        val doc: DocumentReference = firestore.collection("users").document(id)
        doc.set(user)
        Result.Success(id)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    override suspend fun isUserVerified(userId: String): Result<Boolean> = try {
        val doc = firestore.collection("users").document(userId).get()
        val u = doc.data<UserInfo>()
        Result.Success(true)
    } catch (t: Throwable) {
        Result.Error(t)
    }

    fun calculateTotalPrice(products: List<CartEntity>): Long =
        products.sumOf { it.price?.toLong()?.times(it.qty ?: 0) ?: 0 }

}