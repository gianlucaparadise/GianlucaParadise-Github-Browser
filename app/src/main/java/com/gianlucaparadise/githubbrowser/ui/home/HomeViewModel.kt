package com.gianlucaparadise.githubbrowser.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaparadise.githubbrowser.repository.GithubRepository

class HomeViewModel @ViewModelInject constructor(githubRepository: GithubRepository) : ViewModel() {

    private val repoResult = githubRepository.retrieveAuthenticatedUserRepos(viewModelScope)

    val repos = repoResult.pagedList
    val networkState = repoResult.networkState
}
