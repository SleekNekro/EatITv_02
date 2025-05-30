package com.estoyDeprimido.ui.states

import com.estoyDeprimido.data.model.http_.UserResponse

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userData: UserResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
