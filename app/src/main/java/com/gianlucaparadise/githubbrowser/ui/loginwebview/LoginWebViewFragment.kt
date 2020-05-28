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
import androidx.lifecycle.ViewModelProviders

import com.gianlucaparadise.githubbrowser.R
import kotlinx.android.synthetic.main.login_web_view_fragment.*

class LoginWebViewFragment : Fragment() {

    private lateinit var viewModel: LoginWebViewViewModel

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
        viewModel = ViewModelProviders.of(this).get(LoginWebViewViewModel::class.java)

        login_webview.loadUrl(viewModel.authUrl)
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
