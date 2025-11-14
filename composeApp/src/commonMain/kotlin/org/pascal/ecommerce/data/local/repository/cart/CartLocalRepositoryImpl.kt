package org.pascal.ecommerce.data.local.repository.cart

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.database.AppDatabase
import org.pascal.ecommerce.data.local.entity.CartEntity

@Single
class CartLocalRepositoryImpl(
    private val database: AppDatabase,
) : CartLocalRepository {

    override suspend fun getCartById(id: Long): CartEntity? {
        return database.cartDao().getCartById(id)
    }

    override suspend fun getAllCart(): List<CartEntity> {
        return database.cartDao().getAllCart()
    }

    override suspend fun deleteCartById(item: CartEntity) {
        return database.cartDao().deleteCart(item)
    }

    override suspend fun deleteCart() {
        return database.cartDao().clearCart()
    }

    override suspend fun insertCart(item: CartEntity) {
        return database.cartDao().insertCart(item)
    }
}