package com.gianlucaparadise.githubbrowser.ui.searchtabs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchTabsViewModel : ViewModel() {
    companion object {
        const val tag = "SearchTabsViewModel"
    }

    val searchQuery = MutableLiveData("")

    private val onSearchQueryChanged = Observer<String> {
        // This callback is called as soon as the fragment starts
        Log.d(tag, "Search: $it")
        waitAndSearch(query = it)
    }

    private fun search(query: String) = Log.d(tag, "Searching: $query")

    init {
        searchQuery.observeForever(this.onSearchQueryChanged)
    }

    private var ongoingDelay: Job? = null
    private fun waitAndSearch(query: String) {
        ongoingDelay?.cancel()

        if (query.isBlank()) {
            // When query is empty, I don't want to wait because probably results are cached
            search("")
            return
        }

        ongoingDelay = viewModelScope.launch {
            // I wait that the user has stopped typing before requesting beers
            Log.d(tag, "Waiting to request: $query")
            delay(500)
            Log.d(tag, "Requesting: $query")
            search(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchQuery.removeObserver(this.onSearchQueryChanged)
    }
}
