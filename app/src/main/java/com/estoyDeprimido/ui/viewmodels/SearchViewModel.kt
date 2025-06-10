package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchResults = MutableLiveData<List<RecipeData>>()
    val searchResults: LiveData<List<RecipeData>> get() = _searchResults

    fun searchRecipes(title: String) {
        viewModelScope.launch {
            try {
                val recipes = RetrofitClient.createApiService(getApplication()).searchRecipes(title)
                _searchResults.postValue(recipes)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error buscando recetas: ${e.message}")
            }
        }
    }
}

