package com.denisgithuku.softkeja.presentation.components.map

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.denisgithuku.softkeja.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapUiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> get() = _uiState

    init {
        savedStateHandle.get<String>(Constants.homeLocation)?.let { latLng ->
            Log.d("latLng", latLng)
            getLatLng(latLng)
        }
    }


    private fun getLatLng(latLng: String) {
        _uiState.update {
            it.copy(
                latLng = latLng
            )
        }
    }

    fun onEvent(event: MapUiEvent) {
        when(event) {
            is MapUiEvent.ToggleFalloutMap -> {

            }
        }
    }
}