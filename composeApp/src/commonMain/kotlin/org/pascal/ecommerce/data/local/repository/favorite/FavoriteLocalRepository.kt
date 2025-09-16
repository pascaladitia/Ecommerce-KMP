package org.pascal.ecommerce.data.local.repository.favorite

import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.local.database.AppDatabase
import org.pascal.ecommerce.data.local.entity.FavoriteEntity

@Single
class FavoriteLocalRepository(
    private val database: AppDatabase,
) : FavoriteLocalRepositoryImpl {

    override suspend fun getFavoriteById(id: Long): FavoriteEntity? {
        return database.favoriteDao().getFavoriteById(id)
    }

    override suspend fun getAllFavorite(): List<FavoriteEntity> {
        return database.favoriteDao().getAllFavorites()
    }

    override suspend fun deleteFavoriteById(item: FavoriteEntity) {
        return database.favoriteDao().deleteFavorite(item)
    }

    override suspend fun deleteFavorite() {
        return database.favoriteDao().clearFavorite()
    }

    override suspend fun insertFavorite(item: FavoriteEntity) {
        return database.favoriteDao().insertFavorite(item)
    }
}