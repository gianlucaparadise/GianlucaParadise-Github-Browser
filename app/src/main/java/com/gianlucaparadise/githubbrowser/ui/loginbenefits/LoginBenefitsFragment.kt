package com.gianlucaparadise.githubbrowser.ui.loginbenefits

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.ui.loginwebview.LoginWebViewFragment
import kotlinx.android.synthetic.main.login_benefits_fragment.*

class LoginBenefitsFragment : Fragment() {

    companion object {
        fun newInstance() = LoginBenefitsFragment()
    }

    private lateinit var viewModel: LoginBenefitsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_benefits_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login.setOnClickListener {
            it.findNavController().navigate(R.id.loginWebViewFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginBenefitsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
