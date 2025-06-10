package com.estoyDeprimido.ui.states

import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.http_.UserResponse

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userData: UserResponse?) : AuthUiState()
    data class SuccessLogin(val userData: UserData) : AuthUiState()  // Para login exitoso
    data class RegistrationSuccess(val message: String) : AuthUiState()  // Para registro exitoso sin usuario
    data class Error(val message: String) : AuthUiState()
    object Loggedout : AuthUiState()
}
