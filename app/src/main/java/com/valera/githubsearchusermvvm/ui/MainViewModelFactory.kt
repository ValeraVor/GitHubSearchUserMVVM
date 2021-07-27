package com.valera.githubsearchusermvvm.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valera.githubsearchusermvvm.ui.downloadsfragment.DownloadsViewModel
import com.valera.githubsearchusermvvm.ui.profilefragment.UserProfileViewModel
import com.valera.githubsearchusermvvm.ui.usersfragment.SearchUserViewModel
import java.lang.IllegalStateException

class MainViewModelFactory(
    private val app: App,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            SearchUserViewModel::class.java -> {
                SearchUserViewModel(app.usersRepository)
            }
            DownloadsViewModel::class.java -> {
                DownloadsViewModel(app.downloadsRepository)
            }
            UserProfileViewModel::class.java -> {
                UserProfileViewModel(app.usersRepository, app.downloadsRepository)
            }
            else -> throw IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}
fun Fragment.factory() = MainViewModelFactory(requireContext().applicationContext as App)