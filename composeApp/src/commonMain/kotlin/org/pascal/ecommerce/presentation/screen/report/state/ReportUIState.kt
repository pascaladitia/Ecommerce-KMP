package org.pascal.ecommerce.presentation.screen.report.state

data class ReportUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isReport: Boolean = false
)