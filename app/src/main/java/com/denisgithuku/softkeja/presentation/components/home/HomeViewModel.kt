package com.denisgithuku.softkeja.presentation.components.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        getTimeOfDay()
        fetchUser()
        fetchHomeCategories()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _uiState.update {
                    it.copy(
                        user = auth.currentUser
                    )
                }

            }
        }
    }

    private fun fetchHomeCategories() {
        viewModelScope.launch {
            withTimeout(5000L) {
                homeRepository.getAllHomeCategories().collect { result ->
                    _uiState.value.clearUserMessages()
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    selectedCategory = result.data?.first()?.name!!,
                                    categories = result.data,
                                    isLoading = false
                                )
                            }
                            fetchHomes(result.data?.first()?.name!!)
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }.also {
                                _uiState.value.addToUserMessage(UserMessage(message = result.error))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchHomes(category: String) {
        viewModelScope.launch {
            withTimeout(1000) {
                homeRepository.getAllHomes(category).collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Success -> {

                            _uiState.update {
                                it.copy(
                                    homes = result.data ?: listOf(),
                                    isLoading = false
                                )
                            }.also {
                                result.data?.forEach {
                                    fetchHomeImageUrl(it.imageUrl)
                                }
                            }
                            Log.d("homes", result.data.toString())

                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addToUserMessage(UserMessage(message = result.error))
                            }
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

                        _uiState.value.homes.map {
                            it.imageUrl = result.data!!
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }.also {
                            _uiState.value.addToUserMessage(UserMessage(message = result.error))
                        }
                    }
                }

            }
        }
    }

    private fun getTimeOfDay() {

    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SignOut -> {
                userSignOut()
            }
            is HomeUiEvent.FilterHomes -> {
                fetchHomes(event.filter).also {
                    _uiState.update {
                        it.copy(
                            selectedCategory = event.filter
                        )
                    }
                }
            }
        }
    }

    private fun userSignOut() {
        auth.signOut().also {
            _uiState.update {
                it.copy(
                    user = null,
                    userSignedIn = false
                )
            }
        }
    }
}
