package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.gianlucaparadise.githubbrowser.data.Repository
import com.gianlucaparadise.githubbrowser.data.RepositoryDataSource

class HomeViewModel : ViewModel() {

    private val pagingConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setInitialLoadSizeHint(15)
        .setEnablePlaceholders(false)
        .build()

    private val sourceLiveData = MutableLiveData<RepositoryDataSource>()
    private val repoDataSourceFactory = object : DataSource.Factory<String, Repository>() {
        override fun create(): DataSource<String, Repository> {
            val source = RepositoryDataSource(viewModelScope)
            sourceLiveData.postValue(source)
            return source
        }
    }

    val repositories: LiveData<PagedList<Repository>> =
        repoDataSourceFactory.toLiveData(pagingConfig)
}
