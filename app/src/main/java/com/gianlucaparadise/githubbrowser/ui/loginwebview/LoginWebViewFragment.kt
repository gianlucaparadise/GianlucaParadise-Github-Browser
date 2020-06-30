package com.gianlucaparadise.githubbrowser.ui.loginwebview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels

import com.gianlucaparadise.githubbrowser.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_web_view_fragment.*

@AndroidEntryPoint
class LoginWebViewFragment : Fragment() {

    private val viewModel: LoginWebViewViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    companion object {
        const val TAG = "LoginWebViewFragment"

        fun newInstance() = LoginWebViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_web_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_webview.settings.javaScriptEnabled = true
        login_webview.webViewClient = GithubLoginWebViewClient()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onLoginCompleted = onLoginCompleted
        login_webview.loadUrl(viewModel.authUrl)
    }

    private val onLoginCompleted = {
        findNavController().navigate(R.id.action_loginWebViewFragment_to_homeFragment)
    }

    inner class GithubLoginWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.d(TAG, "LoginFlow: WebView Loading: ${request?.url}")

            if (viewModel.isAppAuthorized(request?.url)) {
                Log.d(TAG, "LoginFlow: WebView app authorized")
                viewModel.completeLogin(request?.url)
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}
