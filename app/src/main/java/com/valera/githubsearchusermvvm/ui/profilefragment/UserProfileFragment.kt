package com.valera.githubsearchusermvvm.ui.profilefragment

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.api.ApiService
import com.valera.githubsearchusermvvm.db.AppDataBase
import com.valera.githubsearchusermvvm.model.Downloads
import com.valera.githubsearchusermvvm.model.RepositoriesProfile
import com.valera.githubsearchusermvvm.model.User
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.repositories.UsersRepository
import com.valera.githubsearchusermvvm.ui.SearchUserFragment
import com.valera.githubsearchusermvvm.ui.MainViewModel
import com.valera.githubsearchusermvvm.ui.MainViewModelFactory
import kotlinx.android.synthetic.main.user_profile_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserProfileFragment : Fragment() , RecyclerViewClickListenerRepository {

    lateinit var currentUser: User
    private lateinit var factory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api = ApiService()
        val repository = UsersRepository(api)
        val repositoryDownloads = DownloadsRepository(AppDataBase(requireContext()))
        factory = MainViewModelFactory(repository, repositoryDownloads)
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)
        currentUser =  arguments?.getSerializable(SearchUserFragment.SEARCHED_USER)!! as User
        viewModel.getProfile(currentUser.login)

        viewModel.profile.observe(viewLifecycleOwner, Observer { response ->
            Glide.with(imageAvatar)
                .load(response.avatarUrl)
                .into(imageAvatar)
            textLogin.text = response.login
            textCountRepositories.text = response.publicRepos.toString()
            textCountFollowers.text = response.followers.toString()
            textCountFollowing.text = response.following.toString()
        })

        viewModel.getRepositoriesProfile(currentUser.login)

        viewModel.repositoriesProfile.observe(viewLifecycleOwner, Observer { response ->
            recycler_view_repositories.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = RepositoriesAdapter(response, this)
            }
        })

    }

    override fun onRecyclerViewItemClick(view: View, repositories: RepositoriesProfile) {
        when(view.id){
            R.id.imageDownload -> {
                Toast.makeText(requireContext(), "Download ...",Toast.LENGTH_SHORT).show()
                downloadFile(currentUser.login, repositories.name)
                GlobalScope.launch {
                    viewModel.insertDownload(Downloads(login = currentUser.login, nameRepository = repositories.name))
                }
            }
            R.id.layoutClick -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repositories.html_url))
                requireContext().startActivity(intent)
            }

        }
    }

    fun downloadFile(login: String, repositoryName: String) {

        val url = "https://github.com/$login/$repositoryName/archive/master.zip"
        val nameFile = "${login}_${repositoryName}_"
        val request = DownloadManager.Request(Uri.parse(url))
        val title = URLUtil.guessFileName(url, null, null)
        request.setTitle(nameFile+title)
        request.setDescription("Download File wait minute .. . ... ")

        val cookie = CookieManager.getInstance().getCookie(url)
        request.addRequestHeader("cookie", cookie)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameFile+title)

        val downloadManager = requireContext().getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

    }

}
