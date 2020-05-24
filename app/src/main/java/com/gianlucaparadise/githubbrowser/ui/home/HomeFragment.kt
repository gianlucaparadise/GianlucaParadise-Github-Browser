package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.adapters.RepositoryListAdapter
import com.gianlucaparadise.githubbrowser.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)

        val adapter = RepositoryListAdapter()
        binding.repositoryList.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.repositories.observe(viewLifecycleOwner, Observer { result ->
            val adapter = binding.repositoryList.adapter
            if (adapter is RepositoryListAdapter) {
                adapter.submitList(result)
            }
        })
    }
}
