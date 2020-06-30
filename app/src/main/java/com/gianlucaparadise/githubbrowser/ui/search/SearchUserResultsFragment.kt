package com.gianlucaparadise.githubbrowser.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.gianlucaparadise.githubbrowser.R

import com.gianlucaparadise.githubbrowser.adapters.UserClickHandler
import com.gianlucaparadise.githubbrowser.adapters.UserListAdapter
import com.gianlucaparadise.githubbrowser.databinding.SearchResultsFragmentBinding
import com.gianlucaparadise.githubbrowser.repository.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchUserResultsFragment : Fragment() {

    companion object {
        fun newInstance() = SearchUserResultsFragment()
    }

    private lateinit var binding: SearchResultsFragmentBinding
    private val viewModel: SearchViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false)

        val adapter = UserListAdapter(onUserClicked)

        binding.searchResultsList.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // I'm using a ViewModel shared between me and the parent fragment with the SearchBar
        viewModel.users.observe(viewLifecycleOwner, Observer { result ->
            val adapter = binding.searchResultsList.adapter
            if (adapter is UserListAdapter) {
                adapter.submitList(result)
            }
        })

        viewModel.usersNetworkState.observe(viewLifecycleOwner, Observer { state ->
            if (state.status == Status.FAILED) {
                Snackbar
                    .make(requireView(), R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) {
                        // no-op
                    }
                    .show()
            }
        })
    }

    private val onUserClicked: UserClickHandler = { user ->
        val action = SearchTabsFragmentDirections.actionSearchFragmentToUserDetailFragment(user)
        findNavController().navigate(action)
    }
}
