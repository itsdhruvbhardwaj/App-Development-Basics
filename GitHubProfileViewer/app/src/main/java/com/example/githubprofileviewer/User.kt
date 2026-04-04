package com.example.githubprofileviewer
import com.google.gson.annotations.SerializedName

data class User(
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    val name: String?,

    val bio: String?,

    val followers: Int,
    val following: Int,

    @SerializedName("public_repos")
    val publicRepos: Int
)