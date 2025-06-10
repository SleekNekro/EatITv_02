package com.estoyDeprimido.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.estoyDeprimido.data.model.ProfileData
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.http_.FollowersCountResponse
import com.estoyDeprimido.data.model.http_.LoginRequest
import com.estoyDeprimido.data.model.http_.LoginResponse
import com.estoyDeprimido.data.model.http_.RegisterRequest
import com.estoyDeprimido.data.model.http_.RegisterResponse
import com.estoyDeprimido.data.model.http_.UpdateUserRequest
import com.estoyDeprimido.data.remote.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import okhttp3.ResponseBody
import retrofit2.Response

object UserRepository {

    suspend fun login(context: Context, email: String, password: String): Result<LoginResponse> = try {
        val api = RetrofitClient.createApiService(context)
        val response = api.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            val body = response.body()
            Log.d("UserRepository", "Login response JSON: ${Gson().toJson(body)}")
            body?.let { loginResponse ->
                Result.success(loginResponse)
            } ?: Result.failure(Exception("Respuesta vacía del servidor"))
        } else {
            val errorMsg = response.errorBody()?.string()
            Log.d("UserRepository", "Error en el login: $errorMsg")
            Result.failure(Exception("Error en el login: $errorMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

//    suspend fun register(context: Context, email: String, password: String, username: String): Result<RegisterResponse> = try {
//        val api = RetrofitClient.createApiService(context)
//        Log.d("Register", "Username: $username, Email: $email, Password: $password")
//        val request = RegisterRequestImg(
//            email = email,
//            username = username,
//            password = password
//        )
//        val response = api.registerUser(request)
//        if (response.isSuccessful) {
//            Result.success(response.body()!!)
//        } else {
//            Result.failure(Exception("Error en el registro"))
//        }
//    } catch (e: Exception) {
//        Result.failure(e)
//    }

    suspend fun apiGetUserById(context: Context, userId: Long): UserData? {
        Log.d("UserRepository", "🟢 Solicitando datos para userId=$userId")
        val api = RetrofitClient.createApiService(context)
        val response = api.getUserProfile(userId)

        return if (response.isSuccessful) {
            val userData = response.body()
            Log.d("UserRepository", "🟢 Datos obtenidos correctamente: ${userData?.username ?: "Usuario no encontrado"}")
            userData
        } else {
            Log.e("UserRepository", "🔴 Error en la API: ${response.errorBody()?.string()}")
            null
        }
    }
    suspend fun updateProfile(context: Context, profileData: ProfileData, password: String? = null): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Convertir cada campo de texto a RequestBody
                val usernameBody: RequestBody = profileData.username.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailBody: RequestBody = profileData.email.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordBody: RequestBody? = password?.toRequestBody("text/plain".toMediaTypeOrNull())

                // Convertir la imagen de perfil en MultipartBody.Part (si viene definida)
                val profilePicPart: MultipartBody.Part? = profileData.profilePic?.let { uriString ->
                    val uri: Uri = Uri.parse(uriString)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()
                    if (bytes != null) {
                        val requestFile: RequestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("profilePic", "profile.jpg", requestFile)
                    } else {
                        null
                    }
                }

                // Llamamos a la API usando el método multipart
                val response: Response<ResponseBody> = RetrofitClient.createApiService(context)
                    .updateUserMultipart(
                        userId = profileData.id,
                        username = usernameBody,
                        email = emailBody,
                        password = passwordBody,
                        profilePic = profilePicPart
                    )

                if (response.isSuccessful) {
                    Log.d("UserRepository", "✅ Usuario actualizado correctamente")
                    Result.success("Usuario actualizado correctamente")
                } else {
                    Log.e("UserRepository", "❌ Error al actualizar usuario: ${response.code()}")
                    Result.failure(Exception("Error al actualizar usuario: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "❌ Exception: ${e.message}")
                Result.failure(e)
            }
        }
    }
    suspend fun getFollowers(context: Context, userId: Long): FollowersCountResponse {
        val api = RetrofitClient.createApiService(context)
        val response = api.getUserFollowers(userId)
        return response
    }
    suspend fun getFollowing(context: Context, userId: Long): FollowersCountResponse {
        val api = RetrofitClient.createApiService(context)
        val response = api.getUserFollowing(userId)
        return response
    }

}
