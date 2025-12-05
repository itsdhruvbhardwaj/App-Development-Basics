package com.hackathon.myrecipeapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder().baseUrl("www.themealdb.com/api/json/v1/1/")
    .addConverterFactory(GsonConverterFactory.create()).build()

    val recipeService = retrofit.create(ApiService::class.java)

//kon kon se domain call krne hai api se
interface ApiService
{
    @GET("categories.php") //jaise ye wala krna hai
    suspend fun getCategories(): CategoryResponse //call k baad jo json data aayga use meri category class k according Retrofit convert krega, conversion k baad hme object milega we can directly use it in our app
}