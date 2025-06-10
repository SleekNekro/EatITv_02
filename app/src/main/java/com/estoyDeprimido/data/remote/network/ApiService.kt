package com.estoyDeprimido.data.remote.network

import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.http_.CreateRecipeRequest
import com.estoyDeprimido.data.model.http_.FollowersCountResponse
import com.estoyDeprimido.data.model.http_.LikeStatusResponse
import com.estoyDeprimido.data.model.http_.LikesCountResponse
import com.estoyDeprimido.data.model.http_.LoginRequest
import com.estoyDeprimido.data.model.http_.LoginResponse
import com.estoyDeprimido.data.model.http_.RegisterRequest
import com.estoyDeprimido.data.model.http_.RegisterResponeImg
import com.estoyDeprimido.data.model.http_.RegisterResponse
import com.estoyDeprimido.data.model.http_.UpdateUserRequest
import com.estoyDeprimido.data.model.http_.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("auth/register")
    suspend fun registerUser(
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profilePic: MultipartBody.Part? // La imagen se envía como parte del formulario
    ): Response<RegisterResponeImg>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("user/{id}")
    suspend fun getUserProfile(@Path("id") userId: Long): Response<UserData>

    @PATCH("user/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body request: UpdateUserRequest): Response<Unit>

    @GET("follower/{id}/followers/count")
    suspend fun getUserFollowers(@Path("id") userId: Long): FollowersCountResponse

    @GET("follower/{id}/following/count")
    suspend fun getUserFollowing(@Path("id") userId: Long): FollowersCountResponse

    @GET("recipe")
    suspend fun getRecipes(): Response<List<RecipeData>>

    @GET("recipe/recipes/search")
    suspend fun searchRecipes(@Query("title") title: String): List<RecipeData>

    @GET("recipe/all/{id}")
    suspend fun getUserRecipes(@Path("id") userId: Long): Response<List<RecipeData>>

    @POST("like")
    suspend fun addLike(@Body body: RequestBody): Response<Unit>

    @GET("like/recipe/{recipeId}/likesCount")
    suspend fun getRecipeLikesCount(
        @Path("recipeId") recipeId: Long
    ): Response<LikesCountResponse>

    @GET("like/status/{userId}/{recipeId}")
    suspend fun getLikeStatus(
        @Path("userId") userId: Long,
        @Path("recipeId") recipeId: Long
    ): Response<LikeStatusResponse>

    @DELETE("recipe/{id}")
    suspend fun deleteRecipe(@Path("id") recipeId: Long): Response<Unit>

    @PUT("like")
    @FormUrlEncoded
    suspend fun removeLike(
        @Field("userId") userId: Long,
        @Field("recipeId") recipeId: Long
    ): Response<Unit>

    @Multipart
    @POST("/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): UploadResponse


        @Multipart
        @PATCH("user/{id}")
        suspend fun updateUserMultipart(
            @Path("id") userId: Long,
            @Part("username") username: RequestBody,
            @Part("email") email: RequestBody,
            @Part("password") password: RequestBody?,  // Puede ser nulo
            @Part profilePic: MultipartBody.Part?        // La foto, también puede ser nula
        ): Response<ResponseBody> // O la respuesta que definas


    @POST("/recipe")
    suspend fun createRecipe(
        @Body recipe: CreateRecipeRequest
    ): Response<Unit>
}
