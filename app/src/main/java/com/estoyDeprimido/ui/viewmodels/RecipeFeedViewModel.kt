package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.remote.network.KtorSseClient
import com.estoyDeprimido.data.repository.LikeRepository
import com.estoyDeprimido.data.repository.RecipeRepository
import com.estoyDeprimido.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class RecipeFeedViewModel(app: Application) : AndroidViewModel(app) {

    private val _recipes = MutableStateFlow<List<RecipeCardData>>(emptyList())
    val recipes: StateFlow<List<RecipeCardData>> get() = _recipes

    init {
        loadRecipes()
        subscribeToSseEvents(getApplication()) // 🔥 Suscribirse a SSE
    }

    fun loadRecipes() {
        viewModelScope.launch {
            try {
                val userId = UserPreferences.getUserId(getApplication())
                val recipesData = RecipeRepository.getRecipes(getApplication())

                // Define la URL base de tu servidor
                val baseUrl = "https://eatitv03-production.up.railway.app"

                val mergedList = recipesData.map { recipe ->
                    // Verifica la URL de la imagen; si es relativa, constrúyela absoluta
                    val absoluteImageUrl = if (recipe.imageUrl != null && recipe.imageUrl.startsWith("/")) {
                        "$baseUrl${recipe.imageUrl}"
                    } else {
                        recipe.imageUrl
                    }

                    // También obtener el usuario, likes y demás datos
                    val userData = UserRepository.apiGetUserById(getApplication(), recipe.userId) ?: return@map null
                    val freshLikes = LikeRepository.getLikesCount(getApplication(), recipe.id)
                    val userHasLiked = LikeRepository.getLikeStatus(getApplication(), userId!!, recipe.id)

                    RecipeCardData(
                        id = recipe.id,
                        title = recipe.title,
                        description = recipe.description,
                        servings = recipe.servings.toString(),
                        imageUrl = absoluteImageUrl,  // Asigna la URL absoluta
                        createdAt = recipe.createdAt,
                        user = userData,
                        likesCount = freshLikes,
                        isLiked = userHasLiked
                    )
                }.filterNotNull()

                _recipes.value = mergedList
            } catch (e: Exception) {
                Log.e("RecipeFeedViewModel", "Error cargando recetas: ${e.message}")
            }
        }
    }



    private fun subscribeToSseEvents(context: Context) {
        viewModelScope.launch {
            KtorSseClient.initializeToken(context) // 🔥 Recuperar el token antes de iniciar SSE

            while (KtorSseClient.cachedToken.isNullOrEmpty()) {
                println("⏳ Esperando que el token se actualice en KtorSseClient...")
                delay(2000)
            }

            println("✅ Token disponible, iniciando SSE...")

            KtorSseClient.listenToEvents(context).collect { event ->
                when (event["category"]) {
                    "recipe" -> handleRecipeUpdate(event["data"] as Map<String, Any>)
                    "like" -> handleLikeUpdate(event["data"] as Map<String, Any>)
                }
            }
        }
    }



    private fun handleRecipeUpdate(event: Map<String, Any>) {
        when (event["type"]) {
            "create" -> {
                val newRecipe = Json.decodeFromString<RecipeCardData>(event["recipe"].toString())
                _recipes.value = _recipes.value + newRecipe
            }
            "update" -> {
                val updatedRecipe = Json.decodeFromString<RecipeCardData>(event["recipe"].toString())
                _recipes.value = _recipes.value.map {
                    if (it.id == updatedRecipe.id) updatedRecipe else it
                }
            }
            "delete" -> {
                val deletedRecipeId = event["recipeId"] as Long
                _recipes.value = _recipes.value.filterNot { it.id == deletedRecipeId }
            }
        }
    }

    private fun handleLikeUpdate(event: Map<String, Any>) {
        val recipeId = event["recipeId"] as Long
        val newLikesCount = event["likesCount"] as Int
        val userHasLiked = event["hasLiked"] as Boolean // 🔥 Actualizar `isLiked`

        _recipes.value = _recipes.value.map {
            if (it.id == recipeId) it.copy(
                likesCount = newLikesCount,
                isLiked = userHasLiked // 🔥 Ahora el estado de like se actualiza en la UI
            ) else it
        }
    }


    private fun handleCommentUpdate(event: Map<String, Any>) {
        Log.d("SSE", "Nuevo comentario recibido: $event")
        // Implementa la lógica si necesitas actualizar la UI con comentarios nuevos
    }
}
