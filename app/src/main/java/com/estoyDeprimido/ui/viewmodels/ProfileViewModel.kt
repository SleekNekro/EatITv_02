package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.repository.LikeRepository
import com.estoyDeprimido.data.repository.RecipeRepository
import com.estoyDeprimido.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    // Flujos para la lista de recetas del usuario
    private val _recipes = MutableStateFlow<List<RecipeCardData>>(emptyList())
    val recipes: StateFlow<List<RecipeCardData>> get() = _recipes

    // Flujos para los datos del usuario
    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> get() = _user

    // Flujos para los contadores (seguidores, seguidos, recetas)
    private val _followersCount = MutableStateFlow(0)
    val followersCount: StateFlow<Int> get() = _followersCount

    private val _followingCount = MutableStateFlow(0)
    val followingCount: StateFlow<Int> get() = _followingCount

    private val _recipeCount = MutableStateFlow(0)
    val recipeCount: StateFlow<Int> get() = _recipeCount

    private var recipesLoaded = false
    fun loadProfileRecipes() {
        if (recipesLoaded) return
        viewModelScope.launch {
            try {
                // Obtén el userId de las preferencias
                val userId = UserPreferences.getUserId(getApplication())

                // Carga los datos básicos del usuario
                val userData = UserRepository.apiGetUserById(getApplication(), userId!!)
                if(userData != null) {
                    _user.value = userData
                    // Se asume que 'userData' tiene estos campos; de lo contrario, asigna valores por defecto
                    _followersCount.value = userData.followers ?: 0
                    _followingCount.value = userData.following ?: 0
                }

                // Carga todas las recetas
                val recipesData = RecipeRepository.getRecipes(getApplication())
                val baseUrl = "https://eatitv03-production.up.railway.app"

                // Filtra las recetas que pertenecen al usuario
                val userRecipes = recipesData.filter { recipe ->
                    recipe.userId == userId
                }

                // Mapea las recetas para preparar los datos (construir URL, obtener likes, etc.)
                val mergedList = userRecipes.map { recipe ->
                    val absoluteImageUrl = if (recipe.imageUrl != null && recipe.imageUrl.startsWith("/")) {
                        "$baseUrl${recipe.imageUrl}"
                    } else {
                        recipe.imageUrl
                    }
                    // Obtén datos adicionales del usuario que creó la receta
                    // (en este caso podría ser el mismo que userData, pero lo dejamos por si hay variación)
                    val recipeUser = UserRepository.apiGetUserById(getApplication(), recipe.userId)
                        ?: return@map null
                    val freshLikes = LikeRepository.getLikesCount(getApplication(), recipe.id)
                    val userHasLiked = LikeRepository.getLikeStatus(getApplication(), userId!!, recipe.id)

                    RecipeCardData(
                        id = recipe.id,
                        title = recipe.title,
                        description = recipe.description,
                        servings = recipe.servings.toString(),
                        imageUrl = absoluteImageUrl,
                        createdAt = recipe.createdAt,
                        user = recipeUser,
                        likesCount = freshLikes,
                        isLiked = userHasLiked
                    )
                }.filterNotNull()

                // Actualiza el flujo de recetas y el contador
                _recipes.value = mergedList
                _recipeCount.value = mergedList.size
                recipesLoaded = true
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error cargando recetas: ${e.message}")
            }
        }
    }

    fun deleteRecipe(recipeId: Long, onDeletionSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                RecipeRepository.deleteRecipe(getApplication(), recipeId)
                _recipes.value = _recipes.value.filterNot { it.id == recipeId }
                // Actualiza el contador de recetas
                _recipeCount.value = _recipes.value.size
                onDeletionSuccess()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error borrando receta: ${e.message}")
            }
        }
    }
}
