package org.pascal.ecommerce.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.File
import compose.icons.feathericons.X
import kotlinx.coroutines.launch

@Composable
fun CameraGalleryDialog(
    showDialogCapture: Boolean = false,
    isMultiple: Boolean = false,
    onSelect: (List<ByteArray>?) -> Unit,
    onDismiss: () -> Unit
) {
    var showCamera by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val resizeOptions = ResizeOptions(
        width = 1000, // Custom width
        height = 1000, // Custom height
        resizeThresholdBytes = 1 * 512 * 512L, // Custom threshold for 2MB,
        compressionQuality = 1.0 // Adjust compression quality (0.0 to 1.0)
    )
    val galleryLauncher = rememberImagePickerLauncher(
        selectionMode = if (isMultiple)SelectionMode.Multiple(maxSelection = 5)
                        else SelectionMode.Single,
        resizeOptions = resizeOptions,
        scope = scope,
        onResult = { byteArrays ->
            onSelect(byteArrays)
        }
    )

    if (showCamera) {
        Box {
            PeekabooCameraView(
                modifier = Modifier.fillMaxSize(),
                onBack = {
                    showCamera = false
                    onDismiss()
                },
                onCapture = { byteArray ->
                    byteArray?.let {
                        showCamera = false
                        onSelect(listOf(it))
                    }
                    showCamera = false
                },
            )
        }
    }

    if (showDialogCapture) {
        AlertDialog(
            onDismissRequest = {  },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "Select Foto Source"
                    )
                    Icon(
                        imageVector = FeatherIcons.X,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clip(CircleShape)
                            .clickable { onDismiss() }
                    )
                }
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                onDismiss()
                                showCamera = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            CameraGalleryButton(
                                icon = FeatherIcons.Camera,
                                text = "Camera"
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                galleryLauncher.launch()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            CameraGalleryButton(
                                icon = FeatherIcons.File,
                                text = "Gallery"
                            )
                        }
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {},
            dismissButton = {}
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraGalleryBottom(
    showDialogCapture: Boolean = false,
    isMultiple: Boolean = false,
    onSelect: (List<ByteArray>?) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState -> newState != SheetValue.Hidden }
    )
    var showCamera by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val resizeOptions = ResizeOptions(
        width = 1000, // Custom width
        height = 1000, // Custom height
        resizeThresholdBytes = 1 * 512 * 512L, // Custom threshold for 2MB,
        compressionQuality = 0.5 // Adjust compression quality (0.0 to 1.0)
    )
    val galleryLauncher = rememberImagePickerLauncher(
        selectionMode = if (isMultiple)SelectionMode.Multiple(maxSelection = 5)
        else SelectionMode.Single,
        resizeOptions = resizeOptions,
        scope = scope,
        onResult = { byteArrays ->
            onSelect(byteArrays)
        }
    )

    if (showCamera) {
        Box {
            PeekabooCameraView(
                modifier = Modifier.fillMaxSize(),
                onBack = {
                    showCamera = false
                    onDismiss()
                },
                onCapture = { byteArray ->
                    byteArray?.let {
                        showCamera = false
                        onSelect(listOf(it))
                    }
                    showCamera = false
                },
            )
        }
    }

    if (showDialogCapture) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = modalBottomSheetState,
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            content = {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                    onDismiss()
                                }
                            }
                            .size(24.dp),
                        imageVector = FeatherIcons.X,
                        contentDescription = "Dismiss"
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Pilih atau Ambil Foto",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                            showCamera = true
                        },
                        text = "Ambil Foto Kamera",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = LightGray
                    )

                    Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                            galleryLauncher.launch()
                        },
                        text = "Pilih Dari Gallery",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        )
    }

}

@Composable
private fun CameraGalleryButton(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}