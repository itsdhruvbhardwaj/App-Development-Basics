package com.example.recipeapp

data class Category(val idCategory: String,
               val strCategory: String,
               val strCategoryDescription: String)
data class CategoryResponse(val categories: List<Category>)
