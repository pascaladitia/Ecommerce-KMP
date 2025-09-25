package org.pascal.ecommerce.domain.usecase.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.local.entity.ProfileEntity
import org.pascal.ecommerce.data.local.repository.cart.CartLocalRepositoryImpl
import org.pascal.ecommerce.data.local.repository.favorite.FavoriteLocalRepositoryImpl
import org.pascal.ecommerce.data.local.repository.product.ProductLocalRepositoryImpl
import org.pascal.ecommerce.data.local.repository.profile.ProfileLocalRepositoryImpl
import org.pascal.ecommerce.domain.mapper.toDomain
import org.pascal.ecommerce.domain.mapper.toEntity
import org.pascal.ecommerce.domain.model.Product

@Single
class LocalUseCaseImpl(
    private val profileRepo: ProfileLocalRepositoryImpl,
    private val cartRepo: CartLocalRepositoryImpl,
    private val favoriteRepo: FavoriteLocalRepositoryImpl,
    private val productRepo: ProductLocalRepositoryImpl,
) : LocalUseCase {

    // -------- Profile --------
    override fun getProfileById(id: Long): Flow<ProfileEntity?> = flow {
        emit(profileRepo.getProfileById(id))
    }

    override fun getAllProfiles(): Flow<List<ProfileEntity>> = flow {
        emit(profileRepo.getAllProfiles())
    }

    override fun deleteProfileById(item: ProfileEntity): Flow<Unit> = flow {
        emit(profileRepo.deleteProfileById(item))
    }

    override fun insertProfile(item: ProfileEntity): Flow<Unit> = flow {
        emit(profileRepo.insertProfile(item))
    }

    override fun getCartById(id: Long): Flow<CartEntity?> = flow {
        emit(cartRepo.getCartById(id))
    }

    override fun getAllCart(): Flow<List<CartEntity>> = flow {
        emit(cartRepo.getAllCart())
    }

    override fun deleteCartById(item: CartEntity): Flow<Unit> = flow {
        emit(cartRepo.deleteCartById(item))
    }

    override fun deleteCart(): Flow<Unit> = flow {
        emit(cartRepo.deleteCart())
    }

    override fun insertCart(item: CartEntity): Flow<Unit> = flow {
        emit(cartRepo.insertCart(item))
    }

    // -------- Favorite --------
    override fun getFavoriteById(id: Long): Flow<FavoriteEntity?> = flow {
        emit(favoriteRepo.getFavoriteById(id))
    }

    override fun getAllFavorite(): Flow<List<FavoriteEntity>> = flow {
        emit(favoriteRepo.getAllFavorite())
    }

    override fun deleteFavoriteById(item: FavoriteEntity): Flow<Unit> = flow {
        emit(favoriteRepo.deleteFavoriteById(item))
    }

    override fun deleteFavorite(): Flow<Unit> = flow {
        emit(favoriteRepo.deleteFavorite())
    }

    override fun insertFavorite(item: FavoriteEntity): Flow<Unit> = flow {
        emit(favoriteRepo.insertFavorite(item))
    }

    // -------- Product --------
    override fun getProductById(id: Int): Flow<Product?> = flow {
        emit(productRepo.getProductById(id)?.toDomain())
    }

    override fun getAllProduct(): Flow<List<Product>> = flow {
        emit(productRepo.getAllProduct().map { it.toDomain() })
    }

    override fun deleteProductById(item: Product): Flow<Unit> = flow {
        emit(productRepo.deleteProductById(item.toEntity()))
    }

    override fun deleteProduct(): Flow<Unit> = flow {
        emit(productRepo.deleteProduct())
    }

    override fun insertProduct(item: Product): Flow<Unit> = flow {
        emit(productRepo.insertProduct(item.toEntity()))
    }
}
