package org.pascal.ecommerce.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.pascal.ecommerce.ContextUtils
import org.pascal.ecommerce.domain.model.AppInfo
import java.io.File

actual fun getFileNameFromUri(uri: ByteArray?): String {
    return uri?.let {
        "img_${(1..1000).random()}.jpg"
    } ?: "Unknown File"
}

actual fun viewPhoto(uri: ByteArray?) {
    if (uri != null) {
        val context: Context = ContextUtils.context
        val tempFile = File(context.cacheDir, "temp_image.jpg").apply {
            writeBytes(uri)
        }
        val photoUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(photoUri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

actual fun getFileSizeInMB(uri: ByteArray?): String {
    return uri?.let {
        val sizeInMB = it.size / (1024.0 * 1024.0)
        "%.2f MB".format(sizeInMB)
    } ?: "0.00 MB"
}

actual fun <T> downloadJson(
    entity: T?,
    convertToMap: T.() -> Map<String, String?>,
    fileName: String,
    coroutineScope: CoroutineScope,
    onDownloadState: (Boolean, String) -> Unit
) {
    val context = ContextUtils.context
    coroutineScope.launch {
        try {
            val mapData = entity?.convertToMap()?.mapValues { it.value ?: "" }
            if (mapData == null) {
                onDownloadState(false, "Entity conversion returned null")
                return@launch
            }

            val jsonString = Json.encodeToString(MapSerializer(String.serializer(), String.serializer()), mapData)

            val destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                ?: context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

            val jsonFile = File(destinationDir, "${fileName}_${getCurrentDate()}.json")
            jsonFile.writeText(jsonString)

            onDownloadState(true, "Download $fileName sukses")
        } catch (e: IOException) {
            onDownloadState(false, "File creation failed: ${e.message}")
        } catch (e: Exception) {
            onDownloadState(false, "Unexpected error: ${e.message}")
        }
    }
}

fun Context.getAppInfo(): AppInfo {
    val pm = packageManager
    val appName = applicationInfo.loadLabel(pm).toString()
    val versionName = pm.getPackageInfo(packageName, 0).versionName ?: "Unknown"
    return AppInfo(appName, versionName)
}

actual fun getAppInfo(): AppInfo {
    val context = ContextUtils.context
    return context.getAppInfo()
}

actual fun showToast(message: String) {
    val context = ContextUtils.context
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

actual fun isOnline(): Boolean {
    val context = ContextUtils.context
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)}