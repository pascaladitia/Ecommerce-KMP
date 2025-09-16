package org.pascal.ecommerce.data.remote.dtos

import org.pascal.ecommerce.data.local.entity.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val limit: Int? = null,
    val products: List<ProductEntity>? = null,
    val skip: Int? = null,
    val total: Int? = null
)