package com.example.githubprofileviewer

data class User(
    val login: String,
    val avatar_url: String,
    val bio: String?,
    val followers: Int
)