package com.valera.githubsearchusermvvm.ui.downloadsfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.ui.factory
import kotlinx.android.synthetic.main.downloads_fragment.*

class DownloadsFragment : Fragment(R.layout.downloads_fragment) {

    private val viewModel: DownloadsViewModel by viewModels { factory() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.downloads.observe(viewLifecycleOwner, Observer { response ->
            recycler_view_downloads.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = DownloadsAdapter(response)
            }
        })

    }
}