package com.denisgithuku.softkeja.presentation.components.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Constants
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val firebaseAuth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeDetailsUiState())
    val uiState: StateFlow<HomeDetailsUiState> get() = _uiState

    init {
        _uiState.update {
            it.copy(
                userId = firebaseAuth.currentUser?.uid
            )
        }
        savedStateHandle.get<String>(Constants.homeId)?.let { homeId ->
            fetchHomeById(homeId)
        }

        getBookMarks()
    }

    fun onEvent(event: HomeDetailsUiEvent) {
        when (event) {
            is HomeDetailsUiEvent.BookMarkHome -> {
                _uiState.value.clearUserMessages()
                if (_uiState.value.bookMarks.any { it == event.home }) {
                    _uiState.value.addUserMessage(
                        UserMessage(
                            message = Throwable(
                                message = "Home already bookmarked"
                            )
                        )
                    )
                } else {
                    bookMarkHome(_uiState.value.userId.toString(), event.home)
                }
            }
        }
    }


    private fun bookMarkHome(userId: String, home: Home) {
        viewModelScope.launch {
            homeRepository.addHomeToBookMarks(userId, home).collect { result ->
                when (result) {
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
                                hasBookmarked = true
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }.also {
                            _uiState.value.addUserMessage(
                                UserMessage(
                                    message = result.error
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun fetchHomeById(homeId: String) {
        viewModelScope.launch {
            homeRepository.getHomeById(homeId).collect { result ->
                _uiState.value.clearUserMessages()
                when (result) {
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
                            )
                        }.also {
                            _uiState.value.addUserMessage(UserMessage(message = result.error))
                        }
                    }

                }
            }
        }
    }

    private fun fetchHomeImageUrl(homeImageRef: String) {
        viewModelScope.launch {
            homeRepository.getHomeImageUrl(homeImageRef).collect { result ->
                when (result) {
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
                            it.copy(isLoading = false)
                        }.also {
                            _uiState.value.addUserMessage(UserMessage(message = result.error))
                        }
                    }
                }

            }
        }
    }

    private fun getBookMarks() {
        viewModelScope.launch {
            homeRepository.getBookmarkedHomes().collect { bookmarks ->
                _uiState.update {
                    it.copy(bookMarks = bookmarks)
                }
            }
        }
    }

}
