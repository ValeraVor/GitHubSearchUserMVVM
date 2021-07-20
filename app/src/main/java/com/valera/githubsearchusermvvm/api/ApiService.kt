package com.valera.githubsearchusermvvm.api

import com.valera.githubsearchusermvvm.model.Profile
import com.valera.githubsearchusermvvm.model.RepositoriesProfile
import com.valera.githubsearchusermvvm.model.Users
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getUsers(@Query("q") query: String): Response<Users>

    @GET("users/{login}")
    suspend fun getProfile(@Path("login") login: String): Response<Profile>

    @GET("users/{login}/repos")
    suspend fun getRepositoriesProfile(@Path("login") login: String): Response<List<RepositoriesProfile>>

    companion object {
        operator fun invoke() : ApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()
                .create(ApiService::class.java)
        }
    }

}