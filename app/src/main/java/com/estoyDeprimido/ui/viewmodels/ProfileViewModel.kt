package com.estoyDeprimido.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.repository.RecipeRepository
import com.estoyDeprimido.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileViewModel(private val context: Context, private val userId: Long) : ViewModel() {
    private val _user = mutableStateOf<UserData?>(null)
    val user: MutableState<UserData?> = _user

    private val _recipes = mutableStateOf<List<RecipeCardData>>(emptyList())
    val recipes: MutableState<List<RecipeCardData>> = _recipes

    private val _followersCount = mutableStateOf(0)
    val followersCount: MutableState<Int> = _followersCount

    private val _followingCount = mutableStateOf(0)
    val followingCount: MutableState<Int> = _followingCount

    private val _recipeCount = mutableStateOf(0)
    val recipeCount: MutableState<Int> = _recipeCount

    init {
        viewModelScope.launch {
            try {
                val userDeferred = async { UserRepository.apiGetUserById(context, userId) }
                val recipesDeferred = async { RecipeRepository.getUserRecipes(context, userId) }
                val followersDeferred = async { UserRepository.getFollowers(context, userId) }
                val followingDeferred = async { UserRepository.getFollowing(context, userId) }

                _user.value = userDeferred.await()
                val rawRecipes = recipesDeferred.await()

                _recipes.value = rawRecipes.map { recipe ->
                    RecipeCardData(
                        id = recipe.id,
                        title = recipe.title,
                        description = recipe.description,
                        servings = recipe.servings,
                        imageUrl = recipe.imageUrl,
                        createdAt = recipe.createdAt,
                        user = _user.value,
                        likesCount = 0,
                        isLiked = false
                    )
                }

                _recipeCount.value = rawRecipes.size

                // ✅ Corrección: Actualizar los seguidores correctamente sin usar `ProfileData`
                _followersCount.value = followersDeferred.await().followers_count.toInt()
                _followingCount.value = followingDeferred.await().following_count.toInt()

                Log.d("ProfileViewModel", "🟢 Datos cargados correctamente con seguidores y recetas")

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "🔴 Error al cargar datos: ${e.localizedMessage}")
            }
        }
    }
}
