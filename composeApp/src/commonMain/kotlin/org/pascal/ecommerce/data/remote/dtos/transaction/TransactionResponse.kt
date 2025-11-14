package org.pascal.ecommerce.data.remote.dtos.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    @SerialName("redirect_url")
    val redirectUrl: String? = null
)

