package com.estoyDeprimido.data.model

import kotlinx.serialization.Serializable

data class UserData(
    val id: Long,
    val username: String,
    val email: String,
    val profilePic: String?,
    val recipesCount: Int,
    val followers: Int,
    val following: Int ,
    val createdAt: String
)

@Serializable
data class ProfileData(
    val id: Long,
    val username: String,
    val email: String,
    val profilePic: String? = null
)
