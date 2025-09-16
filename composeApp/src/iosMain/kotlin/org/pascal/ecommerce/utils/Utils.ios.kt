package org.pascal.ecommerce.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.pascal.ecommerce.domain.model.AppInfo
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.stringWithFormat
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
    }
}

actual fun getFileNameFromUri(uri: ByteArray?): String {
    return uri?.let {
        "img_${(1..1000).random()}.jpg"
    } ?: "Unknown File"
}

actual fun viewPhoto(uri: ByteArray?) {
    uri?.let {
        val tempDir = NSTemporaryDirectory()
        val tempFile = "$tempDir/temp_image.jpg"

        val nsData = it.toNSData()
        if (!nsData.writeToFile(tempFile, true)) {
            println("Failed to write image to temporary file.")
            return
        }

        val fileUrl = NSURL.fileURLWithPath(tempFile)

        if (UIApplication.sharedApplication.canOpenURL(fileUrl)) {
            UIApplication.sharedApplication.openURL(fileUrl)
        } else {
            UIApplication.sharedApplication.openURL(fileUrl, options = emptyMap<Any?, Any?>()) { success ->
                if (!success) {
                    println("Failed to open file: $fileUrl")
                }
            }
        }
    }
}

actual fun getFileSizeInMB(uri: ByteArray?): String {
    return uri?.let {
        val sizeInMB = it.size / (1024.0 * 1024.0)
        NSString.stringWithFormat("%.2f MB", sizeInMB)
    } ?: "0.00 MB"
}

actual fun <T> downloadJson(
    entity: T?,
    convertToMap: T.() -> Map<String, String?>,
    fileName: String,
    coroutineScope: CoroutineScope,
    onDownloadState: (Boolean, String) -> Unit
) {
    coroutineScope.launch {
        try {
            val mapData = entity?.convertToMap()?.mapValues { it.value ?: "" }
            if (mapData == null) {
                onDownloadState(false, "Entity conversion returned null")
                return@launch
            }

            val jsonString = Json.encodeToString(MapSerializer(String.serializer(), String.serializer()), mapData)

            val documentsPath = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).first() as String
            val filePath = "$documentsPath/${fileName}_${getCurrentDate()}.json"

            // Write JSON to file
            val fileManager = NSFileManager.defaultManager
            val data = jsonString.encodeToByteArray().toNSData()
            fileManager.createFileAtPath(filePath, data, null)

            onDownloadState(true, "Download $fileName sukses")
        } catch (e: Exception) {
            onDownloadState(false, "Unexpected error: ${e.message}")
        }
    }
}

actual fun getAppInfo(): AppInfo {
    val bundle = NSBundle.mainBundle
    val appName = bundle.objectForInfoDictionaryKey("CFBundleName") as? String ?: "Unknown"
    val version = bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "Unknown"
    return AppInfo(appName, version)
}