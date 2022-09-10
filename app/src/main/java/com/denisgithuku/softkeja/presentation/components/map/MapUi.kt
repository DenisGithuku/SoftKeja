package com.denisgithuku.softkeja.presentation.components.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.R
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
fun MapUi(
    scaffoldState: ScaffoldState,
    mapUiViewModel: MapUiViewModel = hiltViewModel()
) {

    val uiState = mapUiViewModel.uiState.collectAsState().value
    val pos = LatLng(
        uiState.latLng.substringBefore(",").toDouble(),
        uiState.latLng.substringAfter(" ").toDouble()
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pos, 6f)
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }
    val properties by remember { mutableStateOf(MapProperties(
            mapStyleOptions = MapStyleOptions(MapStyle.json),
            isMyLocationEnabled = true,
            isBuildingEnabled = true,
            isTrafficEnabled = true
        ))
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = pos),
                title = uiState.latLng,
                snippet = "Home location",
            )
        }
        Switch(
            modifier = Modifier.offset(12.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primary
            ),
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )

    }

}
