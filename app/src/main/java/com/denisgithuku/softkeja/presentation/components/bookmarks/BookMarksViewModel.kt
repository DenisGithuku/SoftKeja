package com.denisgithuku.softkeja.presentation.components.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarksViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(BookMarksUiState())
    val uiState: StateFlow<BookMarksUiState> get() = _uiState
    
    init {
        fetchBookMarkedHomes()
    }
    
    @OptIn(InternalCoroutinesApi::class)
    private fun fetchBookMarkedHomes() {
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
                                isLoading = false,
                                bookmarks = result.data!!
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

}