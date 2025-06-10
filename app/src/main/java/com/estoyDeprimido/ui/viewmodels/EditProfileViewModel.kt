package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.ProfileData
import com.estoyDeprimido.data.repository.UserRepository
import com.estoyDeprimido.ui.states.EditProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: StateFlow<EditProfileUiState> = _uiState

    private val _editProfileState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)

    fun saveChanges(newProfileData: ProfileData) {
        viewModelScope.launch {
            _editProfileState.value = EditProfileUiState.Loading
            try {

                val result = UserRepository.updateProfile(getApplication(), newProfileData)
                result.fold(
                    onSuccess = {
                        _editProfileState.value = EditProfileUiState.Saved
                    },
                    onFailure = { error ->
                        _editProfileState.value = EditProfileUiState.Error(error.message ?: "Error desconocido")
                    }
                )
            } catch (e: Exception) {
                _editProfileState.value = EditProfileUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }


    private suspend fun updateProfile(newProfileData: ProfileData) {

        UserRepository.updateProfile(getApplication(), newProfileData)
    }
}
