package com.gianlucaparadise.githubbrowser.ui.loginbenefits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels

import com.gianlucaparadise.githubbrowser.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_benefits_fragment.*

@AndroidEntryPoint
class LoginBenefitsFragment : Fragment() {

    companion object {
        fun newInstance() = LoginBenefitsFragment()
    }

    private val viewModel: LoginBenefitsViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_benefits_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginBenefitsFragment_to_loginWebViewFragment)
        }
    }

}
