package com.valera.githubsearchusermvvm.ui.downloadsfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valera.githubsearchusermvvm.model.Downloads
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.utils.Coroutines
import kotlinx.coroutines.Job

class DownloadsViewModel(private val downloadsRepository: DownloadsRepository) : ViewModel() {

    private lateinit var job: Job

    private val _downloads = MutableLiveData<MutableList<Downloads>>()
    val downloads: LiveData<MutableList<Downloads>>
        get() = _downloads

    init {
        getDownloads()
    }

    fun getDownloads() {
        job = Coroutines.ioThenMain(
            { downloadsRepository.getDownloads() },
            { _downloads.postValue(it) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}