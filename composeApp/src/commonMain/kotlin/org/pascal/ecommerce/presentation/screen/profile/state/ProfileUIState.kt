package org.pascal.ecommerce.presentation.screen.profile.state

import org.pascal.ecommerce.domain.model.TransactionModel

data class ProfileUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isVerified: Boolean = false,
    val transactionList: List<TransactionModel> = emptyList()
)
