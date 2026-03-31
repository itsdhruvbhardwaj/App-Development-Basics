package com.example.myapplication.model

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>
)
