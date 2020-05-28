package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.adapters.RepositoryClickHandler

import com.gianlucaparadise.githubbrowser.adapters.RepositoryListAdapter
import com.gianlucaparadise.githubbrowser.databinding.HomeFragmentBinding
import com.gianlucaparadise.githubbrowser.ui.base.BaseMainFragment

class HomeFragment : BaseMainFragment() {

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

        val adapter = RepositoryListAdapter(
            showOwner = false,
            onRepositoryClicked = onRepositoryClicked
        )
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

    private val onRepositoryClicked: RepositoryClickHandler = { repository ->
        findNavController().navigate(R.id.action_homeFragment_to_repositoryDetailFragment)
    }

}
