package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaparadise.githubbrowser.repository.GithubRepository

class HomeViewModel : ViewModel() {

    private val repoResult = GithubRepository.instance.retrieveAuthenticatedUserRepos(viewModelScope)

    val repos = repoResult.pagedList
    val networkState = repoResult.networkState
}
