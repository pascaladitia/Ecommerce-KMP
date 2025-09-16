package org.pascal.ecommerce.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.pascal.ecommerce.data.local.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("SELECT * FROM Product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(entity: ProductEntity)

    @Delete
    suspend fun deleteProduct(entity: ProductEntity)

    @Query("DELETE FROM Product")
    suspend fun clearProduct()
}
