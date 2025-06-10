package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.http_.UpdateUserRequest
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.remote.ApiService
import com.estoyDeprimido.data.remote.RetrofitClient
import com.estoyDeprimido.data.remote.network.KtorSseClient
import com.estoyDeprimido.data.repository.UserRepository
import com.estoyDeprimido.ui.states.AuthUiState
import com.estoyDeprimido.ui.states.EditProfileUiState
import com.estoyDeprimido.utils.restartApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _editProfileState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val editProfileState: StateFlow<EditProfileUiState> get() = _editProfileState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            UserRepository.login(getApplication(), email, password)
                .onSuccess { loginResponse ->
                    RetrofitClient.updateToken(loginResponse.token) // 🔥 Actualiza el token en memoria
                    KtorSseClient.updateToken(loginResponse.token) // 🔥 Actualiza el token en memoria

                    // Guarda el token y los datos del usuario en el DataStore
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
            UserRepository.register(
                context = getApplication(),
                email = email,
                password = password,
                username = username
            )
                .onSuccess { registerResponse ->
                    // Si se devuelve un token, actualízalo; si no, no haces nada
                    registerResponse.token?.let { token ->
                        RetrofitClient.updateToken(token)
                        KtorSseClient.updateToken(token)
                        UserPreferences.saveUser(getApplication(), registerResponse.user, token)
                    }

                    // Actualizamos el estado de éxito, independientemente del token
                    _uiState.value = AuthUiState.Success(registerResponse.user)
                    restartApp(getApplication())
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }
    fun logout(context: Context, onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            UserPreferences.clearUserData(context) // Borra el token y otros datos.
            onLogoutComplete() // Notifica que se completó el logout.
        }
    }
    fun updateUser(
        userId: Long,
        username: String,
        email: String,
        password: String?,
        profilePic: String?
    ) {
        viewModelScope.launch {
            // Indicamos que se inicia la actualización
            _editProfileState.value = EditProfileUiState.Loading
            try {
                val updateRequest = UpdateUserRequest(username, email, password, profilePic)
                val response = RetrofitClient.createApiService(getApplication())
                    .updateUser(userId, updateRequest) // Llamada a Retrofit

                if (response.isSuccessful) {
                    Log.d("AuthViewModel", "✅ Usuario actualizado correctamente")
                    // Actualizamos el estado a 'Saved' para que la UI navegue de vuelta al perfil.
                    _editProfileState.value = EditProfileUiState.Saved
                } else {
                    Log.e("AuthViewModel", "❌ Error al actualizar usuario")
                    _editProfileState.value = EditProfileUiState.Error("Error al actualizar usuario")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "❌ Error: ${e.message}")
                _editProfileState.value = EditProfileUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }




}
