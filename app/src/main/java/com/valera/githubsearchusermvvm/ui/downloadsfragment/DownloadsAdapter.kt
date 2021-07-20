package com.valera.githubsearchusermvvm.ui.downloadsfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.databinding.ItemDownloadBinding
import com.valera.githubsearchusermvvm.model.Downloads

class DownloadsAdapter  (
    private val downloads: MutableList<Downloads>
) : RecyclerView.Adapter<DownloadsAdapter.DownloadsViewHolder>(){

    override fun getItemCount() = downloads.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DownloadsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_download,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        holder.recyclerviewDownloadBinding.downloads = downloads[position]
    }

    inner class DownloadsViewHolder(
        val recyclerviewDownloadBinding: ItemDownloadBinding
    ) : RecyclerView.ViewHolder(recyclerviewDownloadBinding.root)

}