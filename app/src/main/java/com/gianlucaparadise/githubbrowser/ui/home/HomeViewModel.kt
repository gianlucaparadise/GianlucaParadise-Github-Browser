package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gianlucaparadise.githubbrowser.db.AppDatabase
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.data.RepoBoundaryCallback
import com.gianlucaparadise.githubbrowser.repository.GithubRepository

class HomeViewModel : ViewModel() {

    private val repoResult = GithubRepository.instance.retrieveAuthenticatedUserRepos(viewModelScope)

    val repos = repoResult.pagedList
}
