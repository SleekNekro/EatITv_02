package com.estoyDeprimido.data.model.http_

import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.model.UserData
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserResponse
)

@Serializable
data class RecipesResponse(
    val recipes: List<RecipeData>
)

data class UploadResponse(
    val location: String
)

@Serializable
data class RegisterResponse(
    val token: String,
    val user: UserResponse
)

@Serializable
data class RegisterResponeImg(
    val user: UserResponse,
    val profilePic: String,
    val message: String
)

@Serializable
data class ImageUploadResponse(
    val url: String
)

@Serializable
data class FollowResponse(
    val success: Boolean,
    val message: String
)


data class FollowersCountResponse(
    val followers_count: Long,
    val following_count: Long
)


data class LikesCountResponse(
    @SerializedName("likesCount")
    val likesCount: Int
)

@Serializable
data class LikesResponse(val likescount: Int)

@Serializable
data class LikeStatusResponse(val hasLiked: Boolean)

@Serializable
data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val profilePic: String?,
    val followers: Int
) {
    companion object {
        fun fromUser(user: UserData): UserResponse {
            return UserResponse(
                id = user.id,
                username = user.username,
                email = user.email,
                profilePic = user.profilePic,
                followers = user.followers
            )
        }
    }
}