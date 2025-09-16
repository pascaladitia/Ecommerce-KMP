package org.pascal.ecommerce.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import org.pascal.ecommerce.data.local.dao.CartDao
import org.pascal.ecommerce.data.local.dao.FavoriteDao
import org.pascal.ecommerce.data.local.dao.ProductDao
import org.pascal.ecommerce.data.local.dao.ProfileDao
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.local.entity.ProfileEntity

@TypeConverters(Converters::class)
@ConstructedBy(TestDatabaseConstructor::class)
@Database(
    entities = [
        ProfileEntity::class,
        FavoriteEntity::class,
        CartEntity::class,
        ProductEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun profileDao(): ProfileDao
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun productDao(): ProductDao
    override fun clearAllTables(): Unit {}
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object TestDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

interface DB {
    fun clearAllTables(): Unit {}
}