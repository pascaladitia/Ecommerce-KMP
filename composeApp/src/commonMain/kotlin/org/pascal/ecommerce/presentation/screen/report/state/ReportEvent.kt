package org.pascal.ecommerce.presentation.screen.report.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import org.pascal.ecommerce.data.local.entity.CartEntity

val LocalReportEvent = compositionLocalOf { ReportEvent() }

@Stable
data class ReportEvent(
    val onDownload: (List<CartEntity>?) -> Unit = {},
    val onNavBack: () -> Unit = {},
)