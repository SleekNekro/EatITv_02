package com.estoyDeprimido.data.repository

import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.remote.RetrofitClient
import android.content.Context

object RecipeRepository {
    suspend fun getRecipes(context: Context): List<RecipeData> {
        val response = RetrofitClient.createApiService(context).getRecipes()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener recetas: ${response.code()}")
        }
    }
}
