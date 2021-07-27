package com.valera.githubsearchusermvvm.repositories

import com.valera.githubsearchusermvvm.api.ApiService

class UsersRepository(private val api: ApiService) {

    suspend fun getUsers(login: String) = api.getUsers(login)

    suspend fun getProfile(login: String) =  api.getProfile(login)

    suspend fun getRepositoriesProfile(login: String) = api.getRepositoriesProfile(login)

}