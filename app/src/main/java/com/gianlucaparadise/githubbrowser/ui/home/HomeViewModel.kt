package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gianlucaparadise.githubbrowser.data.Repository
import com.gianlucaparadise.githubbrowser.network.BackendService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val repositories: MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>().also {
            loadRepositories()
        }
    }

    fun getRepositories(): LiveData<List<Repository>> {
        return repositories
    }

    private fun loadRepositories() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val repositories = BackendService.retrieveAuthenticatedUserRepositories(10)
                if (repositories?.nodes != null) {
                    this@HomeViewModel.repositories.postValue(repositories.nodes)
                }
            }
        }
    }
}
