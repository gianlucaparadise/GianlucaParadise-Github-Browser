package com.gianlucaparadise.githubbrowser.ui.repodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.gianlucaparadise.githubbrowser.R

import com.gianlucaparadise.githubbrowser.databinding.RepoDetailFragmentBinding
import com.gianlucaparadise.githubbrowser.ui.base.BaseMainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoDetailFragment : BaseMainFragment() {

    companion object {
        fun newInstance() = RepoDetailFragment()
    }

    private val args: RepoDetailFragmentArgs by navArgs()

    private lateinit var binding: RepoDetailFragmentBinding
    private val viewModel: RepoDetailViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RepoDetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.start(args.repo)
        binding.viewmodel = viewModel
    }

}
