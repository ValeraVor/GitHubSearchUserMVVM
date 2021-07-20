package com.valera.githubsearchusermvvm.ui.profilefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.databinding.ItemRepositoryBinding
import com.valera.githubsearchusermvvm.model.RepositoriesProfile

class RepositoriesAdapter (
    private val repositories: List<RepositoriesProfile>,
    private val listener: RecyclerViewClickListenerRepository
) : RecyclerView.Adapter<RepositoriesAdapter.RepositoriesViewHolder>(){

    override fun getItemCount() = repositories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepositoriesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_repository,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RepositoriesViewHolder, position: Int) {
        holder.recyclerviewRepositoryBinding.repositoriesProfile = repositories[position]
        holder.recyclerviewRepositoryBinding.layoutClick.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.recyclerviewRepositoryBinding.layoutClick, repositories[position])
        }
        holder.recyclerviewRepositoryBinding.imageDownload.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.recyclerviewRepositoryBinding.imageDownload, repositories[position])
        }
    }


    inner class RepositoriesViewHolder(
        val recyclerviewRepositoryBinding: ItemRepositoryBinding
    ) : RecyclerView.ViewHolder(recyclerviewRepositoryBinding.root)

}