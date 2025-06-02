package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.data.remote.RetrofitClient
import com.estoyDeprimido.data.repository.LikeRepository
import com.estoyDeprimido.data.repository.RecipeRepository
import com.estoyDeprimido.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class RecipeFeedViewModel(app: Application) : AndroidViewModel(app) {

    private val _recipes = MutableStateFlow<List<RecipeCardData>>(emptyList())
    val recipes: StateFlow<List<RecipeCardData>> get() = _recipes

    // Evitar llamadas excesivas en un rango corto (por ejemplo, 500ms)
    private var lastRecipeLoadTime = 0L
    private val MIN_RELOAD_INTERVAL = 500L

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRecipeLoadTime < MIN_RELOAD_INTERVAL) return
        lastRecipeLoadTime = currentTime

        viewModelScope.launch {
            try {
                // Se obtiene la lista de recetas con caché aplicada.
                val recipesData = RecipeRepository.getRecipes(getApplication())

                // Fusionar información extra para cada receta de forma concurrente.
                val mergedList = recipesData.map { recipe ->
                    async {
                        // Se obtiene la información del usuario y el contador de likes.
                        val userData = UserRepository.apiGetUserById(getApplication(), recipe.userId)
                        val freshLikes = LikeRepository.getLikesCount(getApplication(), recipe.id)
                        RecipeCardData(
                            id = recipe.id,
                            title = recipe.title,
                            description = recipe.description,
                            servings = recipe.servings,
                            imageUrl = recipe.imageUrl,
                            createdAt = recipe.createdAt,
                            user = userData,
                            likesCount = freshLikes,
                            isLiked = false  // O ajusta este valor según tu lógica
                        )
                    }
                }.awaitAll()

                _recipes.value = mergedList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Funciones para actualizar el estado de likes en la lista, evitando llamadas excesivas
    private var lastLikeRequestTime = 0L
    private val MIN_LIKE_INTERVAL = 500L

    fun removeLike(recipeId: Long, userId: Long) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastLikeRequestTime < MIN_LIKE_INTERVAL) return
        lastLikeRequestTime = currentTime

        viewModelScope.launch {
            val result = LikeRepository.removeLike(getApplication(), userId, recipeId)
            if (result.isSuccess) {
                _recipes.value = _recipes.value.map { recipe ->
                    if (recipe.id == recipeId) {
                        recipe.copy(likesCount = max(0, (recipe.likesCount ?: 0) - 1))
                    } else {
                        recipe
                    }
                }
            }
        }
    }

    fun updateRecipeLike(recipeId: Long, liked: Boolean) {
        viewModelScope.launch {
            _recipes.value = _recipes.value.map { recipe ->
                if (recipe.id == recipeId) {
                    recipe.copy(
                        isLiked = liked,
                        likesCount = max(0, (recipe.likesCount ?: 0) + if (liked) 1 else -1)
                    )
                } else {
                    recipe
                }
            }
        }
    }
    fun searchRecipes(title: String) {
        viewModelScope.launch {
            try {
                val recipes = RetrofitClient.createApiService(getApplication()).searchRecipes(title)
                val _searchResults = LiveData<List<RecipeCardData>>
                _searchResults.value = recipes // Donde _searchResults es un LiveData<List<Recipe>>
            } catch (e: Exception) {
                Log.e("SearchError", "Error buscando recetas: ${e.message}")
            }
        }
    }

}
