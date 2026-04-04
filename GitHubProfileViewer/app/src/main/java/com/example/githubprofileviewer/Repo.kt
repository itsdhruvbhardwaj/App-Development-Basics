package com.example.githubprofileviewer

import com.google.gson.annotations.SerializedName

data class Repo(
    val name: String,

    @SerializedName("stargazers_count")
    val stars: Int,

    val language: String?,

    @SerializedName("html_url")
    val url: String,   // cleaner naming

    @SerializedName("updated_at")
    val updatedAt: String
)