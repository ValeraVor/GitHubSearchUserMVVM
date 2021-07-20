package com.valera.githubsearchusermvvm.ui.usersfragment

import android.view.View
import com.valera.githubsearchusermvvm.model.User

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, data: User)
}