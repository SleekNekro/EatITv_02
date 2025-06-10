package com.estoyDeprimido.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estoyDeprimido.ui.viewmodels.RecipeFeedViewModel
import com.estoyDeprimido.utils.RecipeCard

@Composable
fun HomeScreen(feedViewModel: RecipeFeedViewModel = viewModel(), onUserClick: @Composable (Long) -> Unit) {
    val recipesState by feedViewModel.recipes.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        feedViewModel.loadRecipes()
    }

    if (recipesState.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando recetas...", color = Color.Black)
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(92.dp)) }
            items(recipesState, key = { it.id }) { recipe ->
                RecipeCard(recipe)  // Asegúrate de que RecipeCard sepa dibujar la información de RecipeCardData.
            }
            item { Spacer(modifier = Modifier.height(92.dp)) }
        }
    }
}
