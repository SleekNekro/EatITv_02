package com.estoyDeprimido.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estoyDeprimido.ui.viewmodels.RecipeFeedViewModel
import com.estoyDeprimido.utils.RecipeCard

@Composable
fun SearchScreen(
    feedViewModel: RecipeFeedViewModel = viewModel(),
    onUserClick: (Long) -> Unit
) {
    val recipesState by feedViewModel.recipes.collectAsState()
    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }

    // Cargamos las recetas inicialmente si aún no se han cargado
    LaunchedEffect(Unit) {
        feedViewModel.loadRecipes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Campo para introducir la búsqueda
        Spacer(modifier = Modifier.height(150.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar receta") },
            placeholder = { Text("Introduce un título...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Filtramos las recetas cuyo title contenga el string introducido (ignora mayúsculas/minúsculas)
        val filteredRecipes = recipesState.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }

        // Si no hay resultados, mostramos un mensaje
        if (filteredRecipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontraron recetas", color = MaterialTheme.colorScheme.onSurface)
            }
        } else {
            // Mostramos las recetas en una LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Espaciador superior similar a tu HomeScreen
                item { Spacer(modifier = Modifier.height(50.dp)) }
                items(filteredRecipes, key = { it.id }) { recipe ->
                    RecipeCard(recipe)  // Asegúrate de que RecipeCard pueda renderizar RecipeData
                }
                // Espaciador inferior
                item { Spacer(modifier = Modifier.height(92.dp)) }
            }
        }
    }
}
