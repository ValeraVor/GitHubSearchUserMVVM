package com.valera.githubsearchusermvvm.ui.usersfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valera.githubsearchusermvvm.model.Users
import com.valera.githubsearchusermvvm.repositories.UsersRepository
import com.valera.githubsearchusermvvm.utils.Coroutines
import com.valera.githubsearchusermvvm.utils.Resource
import kotlinx.coroutines.Job
import java.io.IOException

class SearchUserViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    private lateinit var job: Job

    private val _users = MutableLiveData<Resource<Users>>()
    val users: LiveData<Resource<Users>>
        get() = _users

    fun getUsers(login: String) {
        _users.postValue(Resource.Loading())
        job = Coroutines.ioThenMain(
            { handleUsersResponse(login) },
            { _users.postValue(it) }
        )
    }
    private suspend fun handleUsersResponse(login: String) =
        try {
            Resource.Success(usersRepository.getUsers(login).body()!!)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error("Network Failure")
                else -> Resource.Error("Conversion Error")
            }
        }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}