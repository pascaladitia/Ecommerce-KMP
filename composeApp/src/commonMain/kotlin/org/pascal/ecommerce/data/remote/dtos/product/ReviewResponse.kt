package org.pascal.ecommerce.data.remote.dtos.product

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val comment: String? = null,
    val date: String? = null,
    val rating: Int? = null,
    val reviewerEmail: String? = null,
    val reviewerName: String? = null
)