package org.pascal.ecommerce.domain.model

import org.pascal.ecommerce.data.remote.dtos.product.ReviewResponse

data class Product(
    val id: Int,
    val availabilityStatus: String,
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val images: List<String>,
    val minimumOrderQuantity: Int,
    val price: Double,
    val rating: Double,
    val returnPolicy: String,
    val review: List<ReviewResponse>,
    val shippingInformation: String,
    val sku: String,
    val stock: Int,
    val tags: List<String>,
    val thumbnail: String,
    val title: String,
    val warrantyInformation: String,
    val weight: Int,
    var isFavorite: Boolean
)