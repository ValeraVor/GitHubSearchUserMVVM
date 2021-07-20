package com.valera.githubsearchusermvvm.ui.profilefragment

import android.view.View
import com.valera.githubsearchusermvvm.model.RepositoriesProfile

interface RecyclerViewClickListenerRepository {
    fun onRecyclerViewItemClick(view: View, data: RepositoriesProfile)
}