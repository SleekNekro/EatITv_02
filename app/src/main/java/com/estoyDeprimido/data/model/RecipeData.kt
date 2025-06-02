package com.estoyDeprimido.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeData(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val servings: Int,
    val imageUrl: String?,
    val createdAt: Long
)

fun RecipeData.toRecipeCardData(): RecipeCardData {
    return RecipeCardData(id, title, description, imageUrl,
        servings.toString(), createdAt, null, null, false)
}
