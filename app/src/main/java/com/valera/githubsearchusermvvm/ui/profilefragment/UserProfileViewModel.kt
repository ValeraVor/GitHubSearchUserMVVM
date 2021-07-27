package com.valera.githubsearchusermvvm.ui.profilefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valera.githubsearchusermvvm.model.Profile
import com.valera.githubsearchusermvvm.model.RepositoriesProfile
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.repositories.UsersRepository
import com.valera.githubsearchusermvvm.utils.Coroutines
import com.valera.githubsearchusermvvm.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class UserProfileViewModel(private val usersRepository: UsersRepository, private val downloadsRepository: DownloadsRepository) : ViewModel() {

    private lateinit var job: Job

    private val _profile = MutableLiveData<Resource<Profile>>()
    val profile: LiveData<Resource<Profile>>
        get() = _profile

    private val _repositoriesProfile = MutableLiveData<Resource<List<RepositoriesProfile>>>()
    val repositoriesProfile: LiveData<Resource<List<RepositoriesProfile>>>
        get() = _repositoriesProfile

    fun getProfile(login: String) {
        _profile.postValue(Resource.Loading())
        job = Coroutines.ioThenMain(
            { handleProfileResponse(login) },
            { _profile.postValue(it) }
        )
    }
    private suspend fun handleProfileResponse(login: String) =
        try {
            Resource.Success(usersRepository.getProfile(login).body()!!)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error("Network Failure")
                else -> Resource.Error("Conversion Error")
            }
        }

    fun getRepositoriesProfile(login: String) {
        _repositoriesProfile.postValue(Resource.Loading())
        job = Coroutines.ioThenMain(
            { handleRepositoriesProfileResponse(login) },
            { _repositoriesProfile.postValue(it) }
        )
    }
    private suspend fun handleRepositoriesProfileResponse(login: String) =
        try {
            Resource.Success(usersRepository.getRepositoriesProfile(login).body()!!)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error("Network Failure")
                else -> Resource.Error("Conversion Error")
            }
        }

    fun downloadFile(login:String, nameRepository:String) {
        downloadsRepository.downloadFile(login, nameRepository)
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }

}