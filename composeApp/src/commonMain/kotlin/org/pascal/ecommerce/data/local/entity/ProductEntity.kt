package org.pascal.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import org.pascal.ecommerce.data.remote.dtos.product.ReviewResponse

@Entity(tableName = "product")
@Serializable
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val availabilityStatus: String? = null,
    val brand: String? = null,
    val category: String? = null,
    val description: String? = null,
    val discountPercentage: Double? = null,
    val images: List<String>? = null,
    val minimumOrderQuantity: Int? = null,
    val price: Double? = null,
    val rating: Double? = null,
    val returnPolicy: String? = null,
    val review: List<ReviewResponse>? = null,
    val shippingInformation: String? = null,
    val sku: String? = null,
    val stock: Int? = null,
    val tags: List<String>? = null,
    val thumbnail: String? = null,
    val title: String? = null,
    val warrantyInformation: String? = null,
    val weight: Int? = null,
    var isFavorite: Boolean? = null
)