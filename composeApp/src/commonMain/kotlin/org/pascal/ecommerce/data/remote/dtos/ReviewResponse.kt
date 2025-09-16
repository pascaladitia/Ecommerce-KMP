package org.pascal.ecommerce.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResonse(
    val comment: String? = null,
    val date: String? = null,
    val rating: Int? = null,
    val reviewerEmail: String? = null,
    val reviewerName: String? = null
)