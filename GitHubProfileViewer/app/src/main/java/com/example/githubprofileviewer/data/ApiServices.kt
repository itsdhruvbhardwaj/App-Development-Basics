package com.example.githubprofileviewer.data

import com.example.githubprofileviewer.Repo
import com.example.githubprofileviewer.User
import com.example.githubprofileviewer.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import okhttp3.OkHttpClient
import okhttp3.Interceptor



object RetrofitInstance {

    private const val BASE_URL = "https://api.github.com/"
    private val TOKEN = BuildConfig.GITHUB_TOKEN // 🔥 replace

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->

            android.util.Log.d("TOKEN_CHECK", "Token: ${BuildConfig.GITHUB_TOKEN}")

            val newRequest = if (TOKEN.isNotEmpty()) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
            } else {
                chain.request()
            }

            chain.proceed(newRequest)
        })
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // ✅ attach here
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

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): Response<List<User>>
}
