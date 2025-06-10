package com.estoyDeprimido.utils

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.estoyDeprimido.data.repository.LikeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable

fun LikeButton(
    context: Context,
    userId: Long,
    recipeId: Long,
    currentLikes: Int,
    onLikeChanged: (Boolean, Int) -> Unit
) {
    var liked by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Inicializa el estado "liked" consultando el repositorio.
    LaunchedEffect(userId, recipeId) {
        coroutineScope.launch {
            liked = LikeRepository.getLikeStatus(context, userId, recipeId)
        }
    }

    IconButton(onClick = {
        coroutineScope.launch {
            if (liked) {
                LikeRepository.removeLike(context, userId, recipeId)
            } else {
                LikeRepository.addLike(context, userId, recipeId)
            }
            liked = !liked

            // CONSULTA el contador actualizado desde la base de datos.
            val updatedCount = LikeRepository.getLikesCount(context, recipeId)
            onLikeChanged(liked, updatedCount)
        }
    }) {
        Icon(
            imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.Favorite,
            contentDescription = "Like",
            tint = if (liked) Color.Red else Color.Gray
        )
    }
}
