package com.gianlucaparadise.githubbrowser.ui.loginwebview

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaparadise.githubbrowser.network.LoginHelper
import kotlinx.coroutines.launch

class LoginWebViewViewModel @ViewModelInject constructor(private val loginHelper: LoginHelper) : ViewModel() {

    private val authDescriptor: LoginHelper.AuthDescriptor =
        loginHelper.buildAuthorizationDescriptor()

    /**
     * Starts the login flow and returns the auth Url for the webview
     */
    val authUrl: String
        get() = authDescriptor.authUrl

    /**
     * Checks if the app has been authorized for the current LoginFlow using the webview url.
     * When this method returns true, the webview can be stopped
     */
    fun isAppAuthorized(url: Uri?): Boolean {
        return loginHelper.isAppAuthorized(url, authDescriptor)
    }

    /**
     * After the app has been authorized, this method retrieves the access token, saves it and closes the login flow
     */
    fun completeLogin(url: Uri?) {
        val loginDescriptor = loginHelper.buildLoginDescriptor(url, authDescriptor)
            ?: throw IllegalStateException("The app is not authorized")

        viewModelScope.launch {
            val accessTokenModel = loginHelper.retrieveAccessToken(loginDescriptor)
            loginHelper.completeLogin(accessTokenModel)
            onLoginCompleted?.invoke()
        }
    }

    /**
     * This callback is called once the login has been completed and the token has been stored
     */
    var onLoginCompleted: (() -> Unit)? = null

    /**
     * I can't find documentation about login error
     */
    var onLoginError: (() -> Unit)? = null
}