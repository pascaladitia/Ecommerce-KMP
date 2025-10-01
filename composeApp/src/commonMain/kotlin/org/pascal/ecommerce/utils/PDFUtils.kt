package org.pascal.ecommerce.utils

import ecommerce_kmp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.InternalResourceApi
import org.pascal.ecommerce.data.local.entity.CartEntity

data class ReportInfo(
    val name: String?,
    val email: String?
)

expect suspend fun generatePdfAndOpen(
    products: List<CartEntity>,
    reportInfo: ReportInfo,
    fileName: String = "Report_${currentFormattedDate()}.pdf",
    logoBytes: ByteArray?
)

expect fun currentFormattedDate(): String

expect fun createReportPdfBytes(
    reportInfo: ReportInfo,
    products: List<CartEntity>,
    logoBytes: ByteArray?
): ByteArray


@OptIn(InternalResourceApi::class)
suspend fun tryLoadLogoBytes(): ByteArray? = runCatching {
    return Res.readBytes("drawable/logo.png")
}.getOrNull()