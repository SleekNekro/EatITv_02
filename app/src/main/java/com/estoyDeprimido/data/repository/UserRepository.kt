package com.estoyDeprimido.data.repository

import android.content.Context
import android.util.Log
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.http_.LoginRequest
import com.estoyDeprimido.data.model.http_.LoginResponse
import com.estoyDeprimido.data.model.http_.RegisterRequest
import com.estoyDeprimido.data.model.http_.RegisterResponse
import com.estoyDeprimido.data.remote.RetrofitClient
import com.google.gson.Gson

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

    suspend fun register(context: Context, username: String, email: String, password: String): Result<RegisterResponse> = try {
        val api = RetrofitClient.createApiService(context)
        val response = api.register(RegisterRequest(username, email, password))
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Error en el registro"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserById(context: Context, userId: Long): UserData {
        val api = RetrofitClient.createApiService(context)
        val response = api.getUserProfile(userId)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Error al obtener datos de usuario: ${response.code()}")
        }
    }
}
