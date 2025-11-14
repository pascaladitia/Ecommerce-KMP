package org.pascal.ecommerce.domain.model

data class BaseProduct(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)