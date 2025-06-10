package com.estoyDeprimido.ui.states

sealed class EditProfileUiState {
    object Idle : EditProfileUiState()
    object Loading : EditProfileUiState()
    object Saved : EditProfileUiState() // Estado que indica que se han guardado los cambios
    data class Error(val message: String) : EditProfileUiState()
}
