package com.valera.githubsearchusermvvm.repositories

import com.valera.githubsearchusermvvm.api.ApiService

class UsersRepository(private val api: ApiService) : SafeApiRequest() {

    suspend fun getUsers(login: String) =  apiRequest { api.getUsers(login) }

    suspend fun getProfile(login: String) =  apiRequest { api.getProfile(login) }

    suspend fun getRepositoriesProfile(login: String) =  apiRequest { api.getRepositoriesProfile(login) }

}