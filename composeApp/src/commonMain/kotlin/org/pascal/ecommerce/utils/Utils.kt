package org.pascal.ecommerce.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.domain.model.AppInfo
import org.pascal.ecommerce.utils.Constant.FORMAT_DATE
import kotlin.time.ExperimentalTime

fun generateRandomChar(): String {
    return ('a'..'z').random().toString()
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun reFormatDate(date: String?): LocalDate? {
    if (date.isNullOrBlank()) {
        return null
    }

    return try {
        return LocalDate.parse(
            input = date,
            format = LocalDate.Format { byUnicodePattern(FORMAT_DATE) }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun calculateTotalPrice(products: List<CartEntity?>): String {
    val total = products.asSequence()
        .filterNotNull()
        .sumOf { (it.price ?: 0.0) * (it.qty ?: 0) }

    return formatTwoDecimals(total)
}

private fun formatTwoDecimals(value: Double): String {
    val scaled = kotlin.math.round(value * 100.0)
    val sign = if (scaled < 0) "-" else ""
    val absScaled = kotlin.math.abs(scaled)
    val whole = (absScaled / 100).toLong()
    val frac = (absScaled % 100).toInt()
    return "$sign$whole.${frac.toString().padStart(2, '0')}"
}

@OptIn(ExperimentalTime::class)
fun getCurrentDate(): String {
    return kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
}

expect fun getFileNameFromUri(uri: ByteArray?): String

expect fun viewPhoto(uri: ByteArray?)

expect fun getFileSizeInMB(uri: ByteArray?): String

expect fun <T> downloadJson(
    entity: T?,
    convertToMap: T.() -> Map<String, String?>,
    fileName: String,
    coroutineScope: CoroutineScope,
    onDownloadState: (Boolean, String) -> Unit
)

expect fun getAppInfo(): AppInfo

expect fun isOnline(): Boolean

expect fun showToast(message: String)
