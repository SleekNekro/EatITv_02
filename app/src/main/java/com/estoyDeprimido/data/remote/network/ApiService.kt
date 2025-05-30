package com.estoyDeprimido.data.remote

import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.http_.LoginRequest
import com.estoyDeprimido.data.model.http_.LoginResponse
import com.estoyDeprimido.data.model.http_.RegisterRequest
import com.estoyDeprimido.data.model.http_.RegisterResponse
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

    @GET("user/{id}")
    suspend fun getUserProfile(@Path("id") userId: Long): Response<UserData>

    @GET("user/{id}/followers")
    suspend fun getUserFollowers(@Path("id") userId: Long): Response<List<UserData>>

    @GET("user/{id}/following")
    suspend fun getUserFollowing(@Path("id") userId: Long): Response<List<UserData>>

    // Nuevo endpoint para obtener recetas
    @GET("recipe")
    suspend fun getRecipes(): Response<List<RecipeData>>
}
