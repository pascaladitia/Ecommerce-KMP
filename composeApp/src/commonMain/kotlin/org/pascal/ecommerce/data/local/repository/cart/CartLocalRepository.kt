package org.pascal.ecommerce.data.local.repository.cart

import org.pascal.ecommerce.data.local.entity.CartEntity

interface CartLocalRepository {
    suspend fun getCartById(id: Long): CartEntity?
    suspend fun getAllCart(): List<CartEntity>
    suspend fun deleteCartById(item: CartEntity)
    suspend fun deleteCart()
    suspend fun insertCart(item: CartEntity)
}