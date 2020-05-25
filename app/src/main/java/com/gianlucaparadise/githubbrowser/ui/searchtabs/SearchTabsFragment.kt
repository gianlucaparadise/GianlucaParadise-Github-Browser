package com.gianlucaparadise.githubbrowser.ui.searchtabs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gianlucaparadise.githubbrowser.adapters.SearchTabsAdapter
import com.gianlucaparadise.githubbrowser.databinding.SearchTabsFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchTabsFragment : Fragment() {

    companion object {
        fun newInstance() = SearchTabsFragment()
    }

    private lateinit var binding: SearchTabsFragmentBinding
    private lateinit var viewModel: SearchTabsViewModel

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
        viewModel = ViewModelProviders.of(this).get(SearchTabsViewModel::class.java)
        binding.viewmodel = viewModel
    }

}
