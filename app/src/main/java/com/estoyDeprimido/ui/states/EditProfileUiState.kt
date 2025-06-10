package com.estoyDeprimido.ui.states

sealed class EditProfileUiState {
    object Idle : EditProfileUiState()
    object Loading : EditProfileUiState()
    object Saved : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
}
