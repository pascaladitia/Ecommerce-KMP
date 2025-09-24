package org.pascal.ecommerce.utils

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import okio.FileSystem
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

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024)
        .build()
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
