package com.gianlucaparadise.githubbrowser.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gianlucaparadise.githubbrowser.adapters.SearchTabsAdapter
import com.gianlucaparadise.githubbrowser.databinding.SearchFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var binding: SearchFragmentBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)

        val adapter = SearchTabsAdapter(this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.pagerTabs, binding.pager) { tab, position ->
            val tabType = SearchTabsAdapter.tabs[position]
            tab.text = getString(tabType.title)
        }.attach()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
