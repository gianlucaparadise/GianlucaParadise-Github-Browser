package com.gianlucaparadise.githubbrowser.ui.repositorydetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gianlucaparadise.githubbrowser.R

class RepositoryDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoryDetailFragment()
    }

    private lateinit var viewModel: RepositoryDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repository_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RepositoryDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
