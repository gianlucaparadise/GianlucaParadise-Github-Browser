package com.gianlucaparadise.githubbrowser.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.gianlucaparadise.githubbrowser.R

import com.gianlucaparadise.githubbrowser.adapters.SearchTabsAdapter
import com.gianlucaparadise.githubbrowser.databinding.SearchTabsFragmentBinding
import com.gianlucaparadise.githubbrowser.ui.base.BaseMainFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTabsFragment : BaseMainFragment() {

    companion object {
        fun newInstance() = SearchTabsFragment()
    }

    private lateinit var binding: SearchTabsFragmentBinding
    private val viewModel: SearchViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchTabsFragmentBinding.inflate(inflater, container, false)

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

        // I'm using a ViewModel shared between me and the child fragments with the search results
        binding.viewmodel = viewModel
    }

}
