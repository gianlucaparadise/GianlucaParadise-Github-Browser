package com.gianlucaparadise.githubbrowser.ui.loginwebview

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaparadise.githubbrowser.network.LoginHelper
import kotlinx.coroutines.launch

class LoginWebViewViewModel : ViewModel() {

    companion object {
        const val TAG = "LoginWebViewViewModel"
    }

    private val authDescriptor: LoginHelper.AuthDescriptor =
        LoginHelper.buildAuthorizationDescriptor()

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
        return LoginHelper.isAppAuthorized(url, authDescriptor)
    }

    /**
     * After the app has been authorized, this method retrieves the access token, saves it and closes the login flow
     */
    fun completeLogin(url: Uri?) {
        val loginDescriptor = LoginHelper.buildLoginDescriptor(url, authDescriptor)
            ?: throw IllegalStateException("The app is not authorized")

        viewModelScope.launch {
            val accessToken = LoginHelper.retrieveAccessToken(loginDescriptor)
            Log.d(TAG, "AccessToken: $accessToken")
        }
    }
}