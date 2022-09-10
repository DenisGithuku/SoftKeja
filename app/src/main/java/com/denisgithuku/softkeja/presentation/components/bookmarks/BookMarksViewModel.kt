package com.denisgithuku.softkeja.presentation.components.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val homeRepository: HomeRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private var _uiState = MutableStateFlow(BookMarksUiState())
    val uiState: StateFlow<BookMarksUiState> get() = _uiState

    init {
        fetchBookMarkedHomes(firebaseAuth.currentUser?.uid.toString())
    }

    private fun fetchBookMarkedHomes(userId: String) {
        viewModelScope.launch {
            homeRepository.getBookmarkedHomes().collect { homes ->
                _uiState.value.clearUserMessages()
                _uiState.update {
                    it.copy(bookmarks = homes)
                }
            }
        }
    }
}
