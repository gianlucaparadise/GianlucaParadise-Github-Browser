package com.gianlucaparadise.githubbrowser.ui.repodetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.gianlucaparadise.githubbrowser.databinding.RepoDetailFragmentBinding
import com.gianlucaparadise.githubbrowser.ui.base.BaseMainFragment

class RepoDetailFragment : BaseMainFragment() {

    companion object {
        fun newInstance() = RepoDetailFragment()
    }

    private val args: RepoDetailFragmentArgs by navArgs()

    private lateinit var binding: RepoDetailFragmentBinding
    private lateinit var viewModel: RepoDetailViewModel

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

        val factory = RepoDetailViewModel.Factory(args.repo)
        viewModel = ViewModelProviders.of(this, factory).get(RepoDetailViewModel::class.java)
        binding.viewmodel = viewModel
    }

}
