package org.pascal.ecommerce.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val redirectUrl: String
)

