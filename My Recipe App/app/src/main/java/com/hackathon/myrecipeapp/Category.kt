package com.hackathon.myrecipeapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
//Converting JSON to Kotlin object
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String) : Parcelable
{}
//You create a response class when there is Unclean JSON data:
data class CategoryResponse(
    val categories: List<Category>
)