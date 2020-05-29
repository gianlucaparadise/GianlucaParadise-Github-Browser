package com.gianlucaparadise.githubbrowser.ui.userdetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.databinding.UserDetailFragmentBinding

class UserDetailFragment : Fragment() {

    companion object {
        fun newInstance() = UserDetailFragment()
    }

    private val args: UserDetailFragmentArgs by navArgs()

    private lateinit var binding: UserDetailFragmentBinding
    private lateinit var viewModel: UserDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserDetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = UserDetailViewModel.Factory(args.user)
        viewModel = ViewModelProviders.of(this, factory).get(UserDetailViewModel::class.java)
        binding.viewmodel = viewModel
    }

}
