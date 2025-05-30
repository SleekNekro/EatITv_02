package com.estoyDeprimido.data.model

data class RecipeCardData(
    val id: Long,
    val title: String,
    val description: String,
    val servings: Int,
    val imageUrl: String?,
    val createdAt: Long,
    val user: UserData
)
