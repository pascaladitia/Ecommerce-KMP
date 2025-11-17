package org.pascal.ecommerce.presentation.screen.maps

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swmansion.kmpmaps.core.CameraPosition
import com.swmansion.kmpmaps.core.Coordinates
import com.swmansion.kmpmaps.core.Map
import com.swmansion.kmpmaps.core.MapProperties
import com.swmansion.kmpmaps.core.MapType
import com.swmansion.kmpmaps.core.MapUISettings
import com.swmansion.kmpmaps.core.Marker
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.pascal.ecommerce.presentation.screen.maps.state.LocalMapsEvent
import org.pascal.ecommerce.theme.AppTheme

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: MapsViewModel = koinInject<MapsViewModel>(),
    onNavBack: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val event = LocalMapsEvent.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalMapsEvent provides event.copy(
            onNavBack = onNavBack
        )
    ) {
        Surface(
            modifier = modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            MapsContent()
        }
    }
}

@Composable
fun MapsContent(
    modifier: Modifier = Modifier
) {
    Map(
        modifier = modifier.fillMaxSize(),
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.NORMAL,
        ),
        uiSettings = MapUISettings(
            myLocationButtonEnabled = true,
            compassEnabled = true
        ),
        cameraPosition = CameraPosition(
            coordinates = Coordinates(latitude = 50.0619, longitude = 19.9373),
            zoom = 13f
        ),
        markers = listOf(
            Marker(
                coordinates = Coordinates(latitude = 50.0486, longitude = 19.9654),
                title = "Software Mansion",
                androidSnippet = "Software house"
            )
        ),
        onMarkerClick = { marker ->
            println("Marker clicked: ${marker.title}")
        },
        onMapClick = { coordinates ->
            println("Map clicked at: ${coordinates.latitude}, ${coordinates.longitude}")
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MapsPreview() {
    AppTheme {
        MapsContent()
    }
}