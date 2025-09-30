package org.pascal.ecommerce.domain.model

import kotlinx.serialization.Serializable
import org.pascal.ecommerce.data.local.entity.CartEntity

@Serializable
data class TransactionModel(
    val userId: String? = null,
    val userName: String? = null,
    val date: String? = null,
    val total: Long = 0L,
    val products: List<CartEntity> = emptyList()
)