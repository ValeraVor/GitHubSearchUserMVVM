package com.valera.githubsearchusermvvm.ui.usersfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.databinding.ItemFoundUsersBinding
import com.valera.githubsearchusermvvm.model.Users

class UsersAdapter(
    private val users: Users,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(){

    override fun getItemCount() = users.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UsersViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_found_users,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.recyclerviewUserBinding.user = users.items[position]
        holder.recyclerviewUserBinding.imageAvatar.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.recyclerviewUserBinding.imageAvatar, users.items[position])
        }
    }

    inner class UsersViewHolder(
        val recyclerviewUserBinding: ItemFoundUsersBinding
    ) : RecyclerView.ViewHolder(recyclerviewUserBinding.root)

}