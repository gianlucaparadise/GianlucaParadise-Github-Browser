package com.gianlucaparadise.githubbrowser.ui.search

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.gianlucaparadise.githubbrowser.data.Repo
import com.gianlucaparadise.githubbrowser.data.SearchRepoResultsDataSource
import com.gianlucaparadise.githubbrowser.data.SearchUserResultsDataSource
import com.gianlucaparadise.githubbrowser.data.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    companion object {
        const val tag = "SearchViewModel"
    }

    val searchQuery = MutableLiveData("")

    private val onSearchQueryChanged = Observer<String> {
        // This callback is called as soon as the fragment starts
        Log.d(tag, "Search: $it")
        waitAndSearch(query = it)
    }

    private val pagingConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setInitialLoadSizeHint(15)
        .setEnablePlaceholders(false)
        .build()

    //region Repos handling
    private val reposSourceLiveData = MutableLiveData<SearchRepoResultsDataSource>()
    private val reposDataSourceFactory = object : DataSource.Factory<String, Repo>() {
        override fun create(): DataSource<String, Repo> {
            val source = SearchRepoResultsDataSource(viewModelScope, searchQuery.value)
            reposSourceLiveData.postValue(source)
            return source
        }
    }

    val repos: LiveData<PagedList<Repo>> = reposDataSourceFactory.toLiveData(pagingConfig)
    private fun updateReposDataSource(query: String) = reposSourceLiveData.value?.updateSearchQuery(query)
    //endregion

    //region Users handling
    private val usersSourceLiveData = MutableLiveData<SearchUserResultsDataSource>()
    private val userDataSourceFactory = object : DataSource.Factory<String, User>() {
        override fun create(): DataSource<String, User> {
            val source = SearchUserResultsDataSource(viewModelScope, searchQuery.value)
            usersSourceLiveData.postValue(source)
            return source
        }
    }

    val users: LiveData<PagedList<User>> = userDataSourceFactory.toLiveData(pagingConfig)
    private fun updateUsersDataSource(query: String) = usersSourceLiveData.value?.updateSearchQuery(query)
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
