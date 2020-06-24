package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.adapters.RepoClickHandler

import com.gianlucaparadise.githubbrowser.adapters.RepoListAdapter
import com.gianlucaparadise.githubbrowser.databinding.HomeFragmentBinding
import com.gianlucaparadise.githubbrowser.repository.Status
import com.gianlucaparadise.githubbrowser.ui.base.BaseMainFragment
import com.google.android.material.snackbar.Snackbar

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

        val adapter = RepoListAdapter(
            showOwner = false,
            onRepoClicked = onRepoClicked
        )
        binding.repoList.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.repos.observe(viewLifecycleOwner, Observer { result ->
            val adapter = binding.repoList.adapter
            if (adapter is RepoListAdapter) {
                adapter.submitList(result)
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer { state ->
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

    private val onRepoClicked: RepoClickHandler = { repo ->
        val action = HomeFragmentDirections.actionHomeFragmentToRepoDetailFragment(repo)
        findNavController().navigate(action)
    }

}
