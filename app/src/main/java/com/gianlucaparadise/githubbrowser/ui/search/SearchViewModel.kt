package com.gianlucaparadise.githubbrowser.ui.search

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gianlucaparadise.githubbrowser.repository.GithubRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ActivityRetainedScoped
class SearchViewModel @ViewModelInject constructor(githubRepository: GithubRepository) : ViewModel() {
    companion object {
        const val tag = "SearchViewModel"
    }

    val searchQuery = MutableLiveData("")

    private val onSearchQueryChanged = Observer<String> {
        // This callback is called as soon as the fragment starts
        Log.d(tag, "Search: $it")
        waitAndSearch(query = it)
    }

    //region Repos handling
    private val repoResult = githubRepository.searchRepos(viewModelScope)

    val repos = repoResult.pagedList
    private fun updateReposDataSource(query: String) = repoResult.search(query)

    val reposNetworkState = repoResult.networkState
    //endregion

    //region Users handling
    private val userResult = githubRepository.searchUsers(viewModelScope)

    val users = userResult.pagedList
    private fun updateUsersDataSource(query: String) = userResult.search(query)

    val usersNetworkState = userResult.networkState
    //endregion

    init {
        searchQuery.observeForever(this.onSearchQueryChanged)
    }

    private var ongoingDelay: Job? = null
    private fun waitAndSearch(query: String) {
        ongoingDelay?.cancel()

        if (query.isBlank()) {
            // When query is empty, I don't want to wait because probably results are cached
            updateReposDataSource("")
            updateUsersDataSource("")
            return
        }

        ongoingDelay = viewModelScope.launch {
            // I wait that the user has stopped typing before requesting beers
            Log.d(tag, "Waiting to request: $query")
            delay(500)
            Log.d(tag, "Requesting: $query")
            updateReposDataSource(query)
            updateUsersDataSource(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchQuery.removeObserver(this.onSearchQueryChanged)
        ongoingDelay?.cancel()
    }
}
