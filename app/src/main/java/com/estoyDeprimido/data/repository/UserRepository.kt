package com.estoyDeprimido.data.repository

import com.estoyDeprimido.data.model.LoginRequest
import com.estoyDeprimido.data.model.LoginResponse
import com.estoyDeprimido.data.model.RegisterRequest
import com.estoyDeprimido.data.model.RegisterResponse
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.UserResponse
import com.estoyDeprimido.data.remote.network.RetrofitClient


object UserRepository {
    private val api = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Result<LoginResponse> = try {
        val response = api.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                Result.success(loginResponse)
            } ?: Result.failure(Exception("Respuesta vacía del servidor"))
        } else {
            Result.failure(Exception("Error en el login: ${response.errorBody()?.string()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }


    suspend fun register(username: String, email: String, password: String): Result<RegisterResponse> = try {
        val response = api.register(RegisterRequest(username, email, password))
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Error en el registro"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
