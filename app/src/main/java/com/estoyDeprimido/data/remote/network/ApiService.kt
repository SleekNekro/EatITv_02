package com.estoyDeprimido.data.remote.network

import com.estoyDeprimido.data.model.LoginRequest
import com.estoyDeprimido.data.model.LoginResponse
import com.estoyDeprimido.data.model.RegisterRequest
import com.estoyDeprimido.data.model.RegisterResponse
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") userId: Long): Response<UserData>

    @GET("users/{id}/followers")
    suspend fun getUserFollowers(@Path("id") userId: Long): Response<List<UserData>>

    @GET("users/{id}/following")
    suspend fun getUserFollowing(@Path("id") userId: Long): Response<List<UserData>>
}
