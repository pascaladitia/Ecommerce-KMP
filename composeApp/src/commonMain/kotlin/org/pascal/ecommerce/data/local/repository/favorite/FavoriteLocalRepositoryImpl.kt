package org.pascal.ecommerce.data.local.repository.favorite

import org.pascal.ecommerce.data.local.entity.FavoriteEntity

interface FavoriteLocalRepositoryImpl {
    suspend fun getFavoriteById(id: Long): FavoriteEntity?
    suspend fun getAllFavorite(): List<FavoriteEntity>
    suspend fun deleteFavoriteById(item: FavoriteEntity)
    suspend fun deleteFavorite()
    suspend fun insertFavorite(item: FavoriteEntity)
}