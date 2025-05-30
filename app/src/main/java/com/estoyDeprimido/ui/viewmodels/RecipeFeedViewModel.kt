package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.data.repository.RecipeRepository
import com.estoyDeprimido.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeFeedViewModel(app: Application) : AndroidViewModel(app) {

    private val _recipes = MutableStateFlow<List<RecipeCardData>>(emptyList())
    val recipes: StateFlow<List<RecipeCardData>> get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            try {
                // Utiliza getApplication() para pasar el contexto a los repositorios
                val recipesData = RecipeRepository.getRecipes(getApplication())
                val mergedList = recipesData.map { recipe ->
                    async {
                        // Obtén la información del usuario utilizando el contexto de la aplicación
                        val userData = UserRepository.getUserById(getApplication(), recipe.userId)
                        RecipeCardData(
                            id = recipe.id,
                            title = recipe.title,
                            description = recipe.description,
                            servings = recipe.servings,
                            imageUrl = recipe.imageUrl,
                            createdAt = recipe.createdAt,
                            user = userData
                        )
                    }
                }.awaitAll()
                _recipes.value = mergedList
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }
}
