package com.estoyDeprimido.data.repository

import android.content.Context
import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.remote.RetrofitClient

object RecipeRepository {
    // Caché en memoria para el feed de recetas y el tiempo de la última actualización.
    private var cachedRecipes: List<RecipeData> = emptyList()
    private var lastFetchTime: Long = 0L

    // Duración de la caché en milisegundos (por ejemplo, 30 segundos).
    private const val CACHE_DURATION = 30_000L

    suspend fun getRecipes(context: Context): List<RecipeData> {
        val currentTimeMillis = System.currentTimeMillis()
        // Si la caché existe y no ha caducado, retornarla.
        if (cachedRecipes.isNotEmpty() && (currentTimeMillis - lastFetchTime < CACHE_DURATION)) {
            return cachedRecipes
        }

        // Si la caché ha caducado o aún no se cargó, realizar la llamada a la API.
        val response = RetrofitClient.createApiService(context).getRecipes()
        if (response.isSuccessful) {
            val recipes = response.body()?: emptyList()
            cachedRecipes = recipes!!
            lastFetchTime = currentTimeMillis
            return recipes
        } else {
            throw Exception("Error al obtener recetas: ${response.code()}")
        }
    }

    // Si deseas que el método de user recipes siga sin caché, lo puedes dejar sin cambios:
    suspend fun getUserRecipes(context: Context, userId: Long): List<RecipeData> {
        val response = RetrofitClient.createApiService(context).getUserRecipes(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener recetas del usuario: ${response.code()}")
        }
    }

    suspend fun deleteRecipe(context: Context, recipeId: Long) {
        val response = RetrofitClient.createApiService(context).deleteRecipe(recipeId)
        if (!response.isSuccessful) {
            throw Exception("Error al eliminar receta: ${response.code()}")
        }
    }
}
