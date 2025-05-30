package com.estoyDeprimido.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.ui.viewmodels.RecipeFeedViewModel
import com.estoyDeprimido.utils.Constants

@Composable
fun HomeScreen(feedViewModel: RecipeFeedViewModel = viewModel()) {
    val recipesState = feedViewModel.recipes.collectAsState()

    if (recipesState.value.isEmpty()) {
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Agrega un Spacer inicial para que la primera card no quede tan pegada al borde
            item {
                Spacer(modifier = Modifier.height(92.dp))
            }
            items(recipesState.value) { recipe ->
                RecipeCard(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: RecipeCardData) {

    val fullImageUrl = if (recipe.imageUrl?.startsWith("http") == true) {
        recipe.imageUrl
    } else {
        Constants.BASE_IMAGE_URL + (recipe.imageUrl ?: "")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera: muestra datos del usuario con texto en negro
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = if (recipe.user.profilePic?.startsWith("http") == true)
                            recipe.user.profilePic
                        else Constants.BASE_IMAGE_URL + (recipe.user.profilePic ?: "")
                    ),
                    contentDescription = "Perfil de ${recipe.user.username}",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recipe.user.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Título y descripción de la receta en negro
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Imagen de la receta
            if (!recipe.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = "Imagen de la receta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
