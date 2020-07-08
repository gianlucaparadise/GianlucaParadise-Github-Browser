package com.gianlucaparadise.githubbrowser.ui.home

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.adapters.RepoClickHandler

import com.gianlucaparadise.githubbrowser.adapters.RepoListAdapter
import com.gianlucaparadise.githubbrowser.databinding.HomeFragmentBinding
import com.gianlucaparadise.githubbrowser.repository.Status
import com.gianlucaparadise.githubbrowser.ui.base.BaseMainFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseMainFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }
    private lateinit var binding: HomeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

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

    private val onRepoClicked: RepoClickHandler = { repo, holder ->
        val title = holder.itemView.findViewById<View>(R.id.header_title).apply {
            transitionName = "title"
        }

        val subtitle = holder.itemView.findViewById<View>(R.id.header_subtitle).apply {
            transitionName = "subtitle"
        }

        val avatar = holder.itemView.findViewById<View>(R.id.header_avatar).apply {
            transitionName = "avatar"
        }

        val description = holder.itemView.findViewById<View>(R.id.repo_short_description).apply {
            transitionName = "description"
        }

        val stars = holder.itemView.findViewById<View>(R.id.repo_total_stars).apply {
            transitionName = "stars"
        }

        val primaryLanguage = holder.itemView.findViewById<View>(R.id.repo_primary_language_name).apply {
            transitionName = "primaryLanguage"
        }

        val extras = FragmentNavigatorExtras(
            title to title.transitionName,
            subtitle to subtitle.transitionName,
            avatar to avatar.transitionName,
            description to description.transitionName,
            stars to stars.transitionName,
            primaryLanguage to primaryLanguage.transitionName
        )

        val action = HomeFragmentDirections.actionHomeFragmentToRepoDetailFragment(repo)
        findNavController().navigate(action, extras)
    }

}
