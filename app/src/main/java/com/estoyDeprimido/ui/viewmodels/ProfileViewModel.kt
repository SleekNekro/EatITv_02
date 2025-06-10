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


    private val _recipes = MutableStateFlow<List<RecipeCardData>>(emptyList())
    val recipes: StateFlow<List<RecipeCardData>> get() = _recipes


    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> get() = _user


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

                val userId = UserPreferences.getUserId(getApplication())


                val userData = UserRepository.apiGetUserById(getApplication(), userId!!)
                if(userData != null) {
                    _user.value = userData

                    _followersCount.value = userData.followers ?: 0
                    _followingCount.value = userData.following ?: 0
                }


                val recipesData = RecipeRepository.getRecipes(getApplication())
                val baseUrl = "https://eatitv03-production.up.railway.app"


                val userRecipes = recipesData.filter { recipe ->
                    recipe.userId == userId
                }


                val mergedList = userRecipes.map { recipe ->
                    val absoluteImageUrl = if (recipe.imageUrl != null && recipe.imageUrl.startsWith("/")) {
                        "$baseUrl${recipe.imageUrl}"
                    } else {
                        recipe.imageUrl
                    }

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

                _recipeCount.value = _recipes.value.size
                onDeletionSuccess()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error borrando receta: ${e.message}")
            }
        }
    }
}
