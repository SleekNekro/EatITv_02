package com.estoyDeprimido.utils

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.estoyDeprimido.data.model.RecipeCardData
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.repository.LikeRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun RecipeCard(
    recipe: RecipeCardData,
    onDeleteClick: (() -> Unit)? = null // Callback opcional para eliminar la receta
) {
    var showPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val userId = runBlocking { UserPreferences.getUserId(context) } ?: -1L
    var likesCount by rememberSaveable { mutableStateOf(recipe.likesCount ?: 0) }
    var isLiked by rememberSaveable { mutableStateOf(recipe.isLiked) }

    LaunchedEffect(recipe.id) {
        coroutineScope.launch {
            likesCount = LikeRepository.getLikesCount(context, recipe.id).coerceAtLeast(0)
            isLiked = LikeRepository.getLikeStatus(context, userId, recipe.id)
            recipe.likesCount = likesCount
            recipe.isLiked = isLiked
        }
    }

    if (showPopup) {
        RecipeDetailPopup(recipe, onDismiss = { showPopup = false })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { showPopup = true },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(recipe.imageUrl),
                contentDescription = "Imagen de la receta",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                recipe.user?.let { Text(text = it.username, color = Color.Black) }
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Por ejemplo el botón de like
                LikeButton(
                    context = context,
                    userId = userId,
                    recipeId = recipe.id,
                    currentLikes = likesCount
                ) { newLikedState, newLikesCount ->
                    Log.d("RecipeCard", "Nuevo contador recibido: $newLikesCount")
                    recipe.likesCount = newLikesCount
                    likesCount = newLikesCount
                    isLiked = newLikedState
                }
                // Si onDeleteClick está definido, mostramos el ícono de eliminar
                if (onDeleteClick != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar receta",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDeleteClick() }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailPopup(recipe: RecipeCardData, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = recipe.title) },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(recipe.imageUrl),
                    contentDescription = "Imagen de la receta",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Por: ${recipe.user?.username ?: "Desconocido"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Comensales: ${recipe.servings}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}
