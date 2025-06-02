package com.estoyDeprimido.data.model

data class RecipeCardData(
    val id: Long,
    val title: String,
    val description: String,
    val servings: String?,
    val imageUrl: String?,
    val createdAt: Long,
    val user: UserData?,
    var likesCount: Int? = null,
    var isLiked: Boolean = false
)
