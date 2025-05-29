package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.estoyDeprimido.data.model.UserResponse
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.repository.UserRepository
import com.estoyDeprimido.ui.states.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            UserRepository.login(email, password)
                .onSuccess { loginResponse ->
                    // Guarda el token y datos del usuario en el DataStore
                    UserPreferences.saveUser(getApplication(), loginResponse.user, loginResponse.token)
                    _uiState.value = AuthUiState.Success(loginResponse.user)
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            UserRepository.register(username, email, password)
                .onSuccess { registerResponse ->
                    UserPreferences.saveUser(getApplication(), registerResponse.user, registerResponse.token)
                    _uiState.value = AuthUiState.Success(registerResponse.user)
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }
}
