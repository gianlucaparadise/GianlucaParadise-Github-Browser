package com.gianlucaparadise.githubbrowser.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gianlucaparadise.githubbrowser.adapters.RepoClickHandler

import com.gianlucaparadise.githubbrowser.adapters.RepoListAdapter
import com.gianlucaparadise.githubbrowser.databinding.SearchResultsFragmentBinding
import java.lang.Exception

class SearchRepoResultsFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRepoResultsFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchResultsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false)

        val adapter = RepoListAdapter(showOwner = true, onRepoClicked = onRepoClicked)

        binding.searchResultsList.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // I'm using a ViewModel shared between me and the parent fragment with the SearchBar
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        } ?: throw Exception("Activity is null, can't get ViewModel")

        viewModel.repos.observe(viewLifecycleOwner, Observer { result ->
            val adapter = binding.searchResultsList.adapter
            if (adapter is RepoListAdapter) {
                adapter.submitList(result)
            }
        })
    }

    private val onRepoClicked: RepoClickHandler = { repo ->
        val action = SearchTabsFragmentDirections.actionSearchFragmentToRepoDetailFragment(repo)
        findNavController().navigate(action)
    }
}
