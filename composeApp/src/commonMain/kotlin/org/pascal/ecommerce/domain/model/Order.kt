package org.pascal.ecommerce.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val redirectUrl: String
)

