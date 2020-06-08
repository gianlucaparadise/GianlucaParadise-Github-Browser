package com.gianlucaparadise.githubbrowser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gianlucaparadise.githubbrowser.data.AppDatabase
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.data.RepoBoundaryCallback

class HomeViewModel : ViewModel() {

    private val pagingConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setInitialLoadSizeHint(15)
        .setEnablePlaceholders(false)
        .build()

    val repos = initializedPagedListBuilder(pagingConfig).build()

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Repo> {

        val database = AppDatabase.instance

        val dataSourceFactory =  database.repoDao().getAll()

        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, config)
        livePagedListBuilder.setBoundaryCallback(RepoBoundaryCallback(viewModelScope, database, config))

        return livePagedListBuilder
    }
}
