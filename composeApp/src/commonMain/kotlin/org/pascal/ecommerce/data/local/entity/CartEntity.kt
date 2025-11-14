package org.pascal.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "cart")
@Serializable
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val imageID: String? = null,
    val category: String? = null,
    val description: String? = null,
    var qty: Int? = null
)
