package com.gianlucaparadise.githubbrowser.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gianlucaparadise.githubbrowser.adapters.RepositoryClickHandler

import com.gianlucaparadise.githubbrowser.adapters.RepositoryListAdapter
import com.gianlucaparadise.githubbrowser.adapters.SearchTabsAdapter
import com.gianlucaparadise.githubbrowser.databinding.SearchResultsFragmentBinding
import java.lang.Exception

class SearchRepositoryResultsFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRepositoryResultsFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchResultsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false)

        val adapter = RepositoryListAdapter(showOwner = true, onRepositoryClicked = onRepositoryClicked)

        binding.searchResultsList.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // I'm using a ViewModel shared between me and the parent fragment with the SearchBar
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        } ?: throw Exception("Activity is null, can't get ViewModel")

        viewModel.repositories.observe(viewLifecycleOwner, Observer { result ->
            val adapter = binding.searchResultsList.adapter
            if (adapter is RepositoryListAdapter) {
                adapter.submitList(result)
            }
        })
    }

    private val onRepositoryClicked: RepositoryClickHandler = { repository ->
        val action = SearchTabsFragmentDirections.actionSearchFragmentToRepositoryDetailFragment(repository)
        findNavController().navigate(action)
    }
}
