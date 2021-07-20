package com.valera.githubsearchusermvvm.ui.downloadsfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.api.ApiService
import com.valera.githubsearchusermvvm.db.AppDataBase
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.repositories.UsersRepository
import com.valera.githubsearchusermvvm.ui.MainViewModel
import com.valera.githubsearchusermvvm.ui.MainViewModelFactory
import kotlinx.android.synthetic.main.downloads_fragment.*

class DownloadsFragment : Fragment() {

    companion object {
        fun newInstance() = DownloadsFragment()
    }

    private lateinit var factory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.downloads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api = ApiService()
        val repository = UsersRepository(api)
        val repositoryDownloads = DownloadsRepository(AppDataBase(requireContext()))
        factory = MainViewModelFactory(repository, repositoryDownloads)
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)

        viewModel.getDownloads()

        viewModel.downloads.observe(viewLifecycleOwner, Observer { response ->
            recycler_view_downloads.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = DownloadsAdapter(response)
            }
        })

    }

}