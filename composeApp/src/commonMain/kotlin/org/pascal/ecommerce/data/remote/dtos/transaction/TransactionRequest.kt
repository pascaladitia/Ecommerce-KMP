package org.pascal.ecommerce.data.remote.dtos.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    @SerialName("transaction_details")
    val transactionDetails: TransactionDetails
)

@Serializable
data class TransactionDetails(
    @SerialName("order_id")
    val orderId: String,
    @SerialName("gross_amount")
    val grossAmount: Int
)
