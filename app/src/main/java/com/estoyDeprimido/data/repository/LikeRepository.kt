package com.estoyDeprimido.data.repository

import android.content.Context
import android.util.Log
import com.estoyDeprimido.data.model.http_.LikeRequest
import com.estoyDeprimido.data.remote.RetrofitClient
import okhttp3.FormBody
import retrofit2.HttpException
import androidx.core.content.edit

object LikeRepository {
    private val likeStatusCache = mutableMapOf<Long, Boolean>()
    private val likesCountCache = mutableMapOf<Long, Int>()

    suspend fun addLike(context: Context, userId: Long, recipeId: Long): Result<Unit> = try {
        val api = RetrofitClient.createApiService(context)
        val body = FormBody.Builder()
            .add("userId", userId.toString())
            .add("recipeId", recipeId.toString())
            .build()

        val response = api.addLike(body)
        if (response.isSuccessful) {
            Log.d("LikeRepository", "Like agregado a receta $recipeId por usuario $userId")
            val updatedLikesCount = getLikesCount(context, recipeId)+1
            updateLikesCount(context, recipeId, updatedLikesCount)
            Result.success(Unit)
        } else {
            val errorMsg = response.errorBody()?.string()
            Log.e("LikeRepository", "Error-1 al agregar el like: $errorMsg")
            Result.failure(Exception("Error al agregar el like: $errorMsg"))
        }
    } catch (e: HttpException) {
        Log.e("LikeRepository", "Error HTTP al agregar like: ${e.code()}")
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("LikeRepository", "Error-2 al agregar like: ${e.localizedMessage}")
        Result.failure(e)
    }

    suspend fun removeLike(context: Context, userId: Long, recipeId: Long): Result<Unit> {
        return try {
            val api = RetrofitClient.createApiService(context)

            Log.d("LikeRepository", "Intentando eliminar Like de receta $recipeId por usuario $userId")

            val response = api.removeLike(userId, recipeId) // ✅ Enviar correctamente


            if (response.isSuccessful) {
                Log.d("LikeRepository", "Like eliminado de receta $recipeId por usuario $userId")

                val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit { remove("likes_receta_$recipeId") } // ✅ Borra la caché ANTES de actualizar

                val updatedLikesCount = getLikesCount(context, recipeId) - 1
                updateLikesCount(context, recipeId, updatedLikesCount)

                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e("LikeRepository", "Error al eliminar el like: $errorMsg")
                Result.failure(Exception("Error al eliminar el like: $errorMsg"))
            }
        } catch (e: HttpException) {
            Log.e("LikeRepository", "Error HTTP al eliminar like: ${e.code()}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("LikeRepository", "Error al eliminar like: ${e.localizedMessage}")
            Result.failure(e)
        }
    }



    suspend fun getLikeStatus(context: Context, userId: Long, recipeId: Long): Boolean {
        val api = RetrofitClient.createApiService(context)
        val response = api.getLikeStatus(userId, recipeId)

        return if (response.isSuccessful) {
            val status = response.body()?.hasLiked ?: false
            likeStatusCache[recipeId] = status // ✅ Asegura que la caché siempre refleje el estado correcto
            status
        } else {
            likeStatusCache[recipeId] ?: false // 🔥 Si la API falla, devuelve la caché (si existe)
        }
    }

    suspend fun getLikesCount(context: Context, recipeId: Long): Int {
        return try {
            val api = RetrofitClient.createApiService(context)
            val response = api.getRecipeLikesCount(recipeId)
            if (response.isSuccessful) {
                response.body()?.likesCount ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun updateLikesCount(context: Context, recipeId: Long, newCount: Int) {
        val sharedPreferences = context.getSharedPreferences("LikesData", Context.MODE_PRIVATE)
        sharedPreferences.edit { putInt("likesCount_$recipeId", newCount) }

        Log.d("LikeRepository", "Likes actualizados en SharedPreferences para receta $recipeId: $newCount")
    }



}
