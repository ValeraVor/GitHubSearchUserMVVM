package com.valera.githubsearchusermvvm.ui.profilefragment

import android.content.BroadcastReceiver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.api.DownloadService
import com.valera.githubsearchusermvvm.model.RepositoriesProfile
import com.valera.githubsearchusermvvm.model.User
import com.valera.githubsearchusermvvm.ui.SearchUserFragment
import com.valera.githubsearchusermvvm.ui.factory
import com.valera.githubsearchusermvvm.ui.usersfragment.UsersAdapter
import com.valera.githubsearchusermvvm.utils.Resource
import kotlinx.android.synthetic.main.search_user_fragment.*
import kotlinx.android.synthetic.main.user_profile_fragment.*


class UserProfileFragment : Fragment(R.layout.user_profile_fragment) , RecyclerViewClickListenerRepository {

    var downloadID : Long = 0L
    lateinit var currentUser: User
    private val viewModel: UserProfileViewModel by viewModels { factory() }
    lateinit var networkStateReceiver: BroadcastReceiver
    lateinit var donwloadServer : DownloadService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donwloadServer = DownloadService(requireContext())
        currentUser =  arguments?.getSerializable(SearchUserFragment.SEARCHED_USER)!! as User
        viewModel.getProfile(currentUser.login)

        viewModel.profile.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is Resource.Success->{
                    recycler_view_users.also {
                        Glide.with(imageAvatar)
                            .load(response.data?.avatarUrl)
                            .into(imageAvatar)
                        textLogin.text = response.data?.login
                        textCountRepositories.text = response.data?.publicRepos.toString()
                        textCountFollowers.text = response.data?.followers.toString()
                        textCountFollowing.text = response.data?.following.toString()
                    }
                }
                is Resource.Error-> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.getRepositoriesProfile(currentUser.login)

        viewModel.repositoriesProfile.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success->{
                    recycler_view_repositories.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.setHasFixedSize(true)
                        it.adapter = RepositoriesAdapter(response.data!!, this)
                    }
                }
                is Resource.Error-> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {}
            }
        })

    }

    override fun onRecyclerViewItemClick(view: View, repositories: RepositoriesProfile) {
        when(view.id){
            R.id.imageDownload -> {
                DownloadService(requireContext()).provideDownload(currentUser.login, repositories.name)
            }
            R.id.layoutClick -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repositories.html_url))
                requireContext().startActivity(intent)
            }

        }
    }

}
