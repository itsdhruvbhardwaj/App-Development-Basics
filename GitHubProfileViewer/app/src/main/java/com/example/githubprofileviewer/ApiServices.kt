package com.example.githubprofileviewer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path



object RetrofitInstance {

    //add base URl
    private const val BASE_URL = "https://api.github.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

//kon kon se domain call krne hai api se
interface ApiService {

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<User>

    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") username: String
    ): Response<List<Repo>>

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): Response<List<User>>
}