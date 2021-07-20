package com.valera.githubsearchusermvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valera.githubsearchusermvvm.model.Downloads
import com.valera.githubsearchusermvvm.model.Profile
import com.valera.githubsearchusermvvm.model.RepositoriesProfile
import com.valera.githubsearchusermvvm.model.Users
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.repositories.UsersRepository
import com.valera.githubsearchusermvvm.utils.Coroutines
import com.valera.githubsearchusermvvm.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(private val usersRepository: UsersRepository, private val downloadsRepository: DownloadsRepository) : ViewModel() {

    private lateinit var job: Job

    private val _users = MutableLiveData<Resource<Users>>()
    val users: LiveData<Resource<Users>>
        get() = _users

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile>
        get() = _profile

    private val _repositoriesProfile = MutableLiveData<List<RepositoriesProfile>>()
    val repositoriesProfile: LiveData<List<RepositoriesProfile>>
        get() = _repositoriesProfile

    private val _downloads = MutableLiveData<MutableList<Downloads>>()
    val downloads: LiveData<MutableList<Downloads>>
        get() = _downloads

    fun getUsers(login: String) {
        _users.postValue(Resource.Loading())
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = usersRepository.getUsers(login)
                _users.postValue(Resource.Success(result!!))
            } catch(t: Throwable) {
                when(t) {
                    is IOException -> _users.postValue(Resource.Error("Network Failure"))
                    else -> _users.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    fun getProfile(login: String) {
        job = Coroutines.ioThenMain(
            { usersRepository.getProfile(login) },
            { _profile.postValue(it) }
        )
    }

    fun getDownloads() {
        job = Coroutines.ioThenMain(
            { downloadsRepository.getDownloads() },
            { _downloads.postValue(it) }
        )
    }

    fun getRepositoriesProfile(login: String) {
        job = Coroutines.ioThenMain(
            { usersRepository.getRepositoriesProfile(login) },
            { _repositoriesProfile.postValue(it) }
        )
    }

    suspend fun insertDownload(download : Downloads) = downloadsRepository.insert(download)

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}