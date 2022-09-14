package com.denisgithuku.softkeja.presentation.components.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> get() = _uiState

    init {
        savedStateHandle.get<String>("userId")?.let { userId ->
            getUserProfileData(userId)
        }
    }

    private fun getUserProfileData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(userId).collectLatest { result ->
                _uiState.value.clearUserMessages()
                when (result) {
                    is Resource.Error -> {
                        _uiState.value.addToUserMessage(userMessage = UserMessage(message = result.error))
                    }
                    is Resource.Loading -> {
                       _uiState.update {
                           it.copy(isLoading = true)
                       }
                    }
                    is Resource.Success -> {
                        val userProfile = UserProfile(
                            userId = result.data?.userId,
                            email = result.data?.email,
                            isPremium = result.data?.isPremium ?: false,
                            firstname = result.data?.firstname,
                            lastname = result.data?.lastname
                        )
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userProfile = userProfile
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(profileEvent: ProfileUiEvent) {
        when(profileEvent) {
            ProfileUiEvent.OnSignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
