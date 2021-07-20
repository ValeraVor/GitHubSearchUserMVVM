package com.valera.githubsearchusermvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.repositories.UsersRepository

class MainViewModelFactory (private val repositoryUsers: UsersRepository, private val repositoryDownloads: DownloadsRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repositoryUsers, repositoryDownloads) as T
    }

}