package com.estoyDeprimido.data.model

data class UserData(
    val id: Long,
    val username: String,
    val email: String,
    val profilePic: String?,
    val followers: Int,
    val createdAt: String
)
