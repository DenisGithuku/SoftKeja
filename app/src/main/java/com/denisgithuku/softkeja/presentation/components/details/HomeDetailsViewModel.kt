package com.denisgithuku.softkeja.presentation.components.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Constants
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _uiState = MutableStateFlow(HomeDetailsUiState())
    val uiState: StateFlow<HomeDetailsUiState> get() = _uiState

    init {
        savedStateHandle.get<String>(Constants.homeId)?.let { homeId ->
                fetchHomeById(homeId)
            }
    }

    fun onEvent(event: HomeDetailsUiEvent) {
        when(event) {
            is HomeDetailsUiEvent.BookMarkHome -> {
                viewModelScope.launch {
                    homeRepository.getBookmarkedHomes().collect { result ->
                        when(result) {
                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = true
                                    )
                                }
                            }
                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                                if(result.data?.contains(event.home) == true) {
                                    _uiState.update { details ->
                                        details.copy(error = "Home already bookmarked")
                                    }
                                } else {
                                    homeRepository.addHomeToBookMarks(event.home).collect { result ->
                                        when(result) {
                                            is Resource.Loading -> {
                                                _uiState.update {
                                                    it.copy(
                                                        isLoading = true
                                                    )
                                                }
                                            }
                                            is Resource.Success -> {
                                                _uiState.update {
                                                    it.copy(
                                                        isLoading = false,
                                                        hasBookmarked = result.data!!
                                                    )
                                                }
                                            }
                                            is Resource.Error -> {
                                                _uiState.update {
                                                    it.copy(
                                                        isLoading = false,
                                                        error = result.error
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = result.error
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun fetchHomeById(homeId: String) {
        viewModelScope.launch {
            homeRepository.getHomeById(homeId).collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                home = result.data!!
                            )
                        }
                        fetchHomeImageUrl(result.data?.imageUrl!!)
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                    }

                }
            }
        }
    }

    private fun fetchHomeImageUrl(homeImageRef: String) {
        viewModelScope.launch {
            homeRepository.getHomeImageUrl(homeImageRef).collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiState.value.home.imageUrl = result.data!!
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, error = result.error)
                        }
                    }
                }

            }
        }
    }

}
