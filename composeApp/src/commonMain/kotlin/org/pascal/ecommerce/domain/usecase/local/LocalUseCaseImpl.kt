package org.pascal.ecommerce.domain.usecase.local

import kotlinx.coroutines.flow.Flow
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.local.entity.ProfileEntity

interface LocalUseCaseImpl {
    // Profile
    fun getProfileById(id: Long): Flow<ProfileEntity?>
    fun getAllProfiles(): Flow<List<ProfileEntity>>
    fun deleteProfileById(item: ProfileEntity): Flow<Unit>
    fun insertProfile(item: ProfileEntity): Flow<Unit>

    // Cart
    fun getCartById(id: Long): Flow<CartEntity?>
    fun getAllCart(): Flow<List<CartEntity>>
    fun deleteCartById(item: CartEntity): Flow<Unit>
    fun deleteCart(): Flow<Unit>
    fun insertCart(item: CartEntity): Flow<Unit>

    // Favorite
    fun getFavoriteById(id: Long): Flow<FavoriteEntity?>
    fun getAllFavorite(): Flow<List<FavoriteEntity>>
    fun deleteFavoriteById(item: FavoriteEntity): Flow<Unit>
    fun deleteFavorite(): Flow<Unit>
    fun insertFavorite(item: FavoriteEntity): Flow<Unit>

    // Product
    fun getProductById(id: Int): Flow<ProductEntity?>
    fun getAllProduct(): Flow<List<ProductEntity>>
    fun deleteProductById(item: ProductEntity): Flow<Unit>
    fun deleteProduct(): Flow<Unit>
    fun insertProduct(item: ProductEntity): Flow<Unit>
}
