package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.http_.UpdateUserRequest
import com.estoyDeprimido.data.preferences.UserPreferences
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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
                    RetrofitClient.updateToken(loginResponse.token)
                    KtorSseClient.updateToken(loginResponse.token)


                    UserPreferences.saveUser(getApplication(), loginResponse.user, loginResponse.token)
                    _uiState.value = AuthUiState.Success(loginResponse.user)
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun register(username: String, email: String, password: String, profilePicUri: Uri?, context: Context) {
        viewModelScope.launch {
            val requestBody = mutableMapOf<String, RequestBody>()
            requestBody["username"] = username.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBody["email"] = email.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBody["password"] = password.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart: MultipartBody.Part? = profilePicUri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                bytes?.toRequestBody("image/jpeg".toMediaTypeOrNull())?.let { requestFile ->
                    MultipartBody.Part.createFormData("profilePic", "profile.jpg", requestFile)
                }
            }

            val response = RetrofitClient.createApiService(context).registerUser(requestBody, imagePart)

            if (response.isSuccessful) {
                Log.d("RegisterViewModel", "✅ Usuario registrado correctamente con foto")
                restartApp(getApplication())
            } else {
                Log.e("RegisterViewModel", "❌ Error al registrar usuario")
            }
        }
    }

    fun logout(context: Context, onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            UserPreferences.clearUserData(context)
            onLogoutComplete()
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

            _editProfileState.value = EditProfileUiState.Loading
            try {
                val updateRequest = UpdateUserRequest(username, email, password, profilePic)
                val response = RetrofitClient.createApiService(getApplication())
                    .updateUser(userId, updateRequest)

                if (response.isSuccessful) {
                    Log.d("AuthViewModel", "✅ Usuario actualizado correctamente")

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
