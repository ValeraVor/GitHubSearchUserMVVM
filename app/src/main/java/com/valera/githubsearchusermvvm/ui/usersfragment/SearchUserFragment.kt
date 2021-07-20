package com.valera.githubsearchusermvvm.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.valera.githubsearchusermvvm.R
import com.valera.githubsearchusermvvm.api.ApiService
import com.valera.githubsearchusermvvm.db.AppDataBase
import com.valera.githubsearchusermvvm.model.User
import com.valera.githubsearchusermvvm.repositories.DownloadsRepository
import com.valera.githubsearchusermvvm.repositories.UsersRepository
import com.valera.githubsearchusermvvm.ui.usersfragment.RecyclerViewClickListener
import com.valera.githubsearchusermvvm.ui.usersfragment.UsersAdapter
import com.valera.githubsearchusermvvm.utils.Resource
import kotlinx.android.synthetic.main.search_user_fragment.*

class SearchUserFragment : Fragment() , RecyclerViewClickListener {

    private lateinit var factory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    companion object {
        const val SEARCHED_USER = "searched_user"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api = ApiService()
        val repositoryUsers = UsersRepository(api)
        val repositoryDownloads = DownloadsRepository(AppDataBase(requireContext()))
        factory = MainViewModelFactory(repositoryUsers, repositoryDownloads)

        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        viewModel.users.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success->{
                    recycler_view_users.also {
                        it.layoutManager = GridLayoutManager(requireContext(),3)
                        it.setHasFixedSize(true)
                        it.adapter = UsersAdapter(response.data!!, this)
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

        search_text.queryHint = getString(R.string.search_hint)
        search_text.isIconifiedByDefault = false
        search_text.setOnQueryTextListener(QueryTextListener {
            viewModel.getUsers(it)
        })

    }

    override fun onRecyclerViewItemClick(view: View, user: User) {
        when(view.id){
            R.id.imageAvatar -> {
                val bundle = Bundle()
                bundle.putSerializable(SEARCHED_USER, user)
                Navigation.findNavController(view).navigate(R.id.action_searchUserFragment_to_userProfileFragment, bundle)
            }
        }
    }
}

class QueryTextListener(private val action: (String) -> Unit) : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null && query.isNotBlank()) action(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

}