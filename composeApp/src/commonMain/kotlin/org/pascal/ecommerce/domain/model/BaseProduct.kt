package org.pascal.ecommerce.domain.model

import org.pascal.ecommerce.data.local.entity.ProductEntity

data class BaseProduct(
    val limit: Int,
    val products: List<ProductEntity>,
    val skip: Int,
    val total: Int
)