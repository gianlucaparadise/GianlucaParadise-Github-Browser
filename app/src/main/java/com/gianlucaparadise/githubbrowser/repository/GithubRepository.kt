package com.gianlucaparadise.githubbrowser.repository

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gianlucaparadise.githubbrowser.data.RepoBoundaryCallback
import com.gianlucaparadise.githubbrowser.data.SearchRepoResultsDataSource
import com.gianlucaparadise.githubbrowser.db.AppDatabase
import com.gianlucaparadise.githubbrowser.vo.Repo
import kotlinx.coroutines.CoroutineScope

class GithubRepository {

    companion object {
        val instance: GithubRepository by lazy {
            GithubRepository()
        }
    }

    private val pagingConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setInitialLoadSizeHint(15)
        .setEnablePlaceholders(false)
        .build()

    fun retrieveAuthenticatedUserRepos(scope: CoroutineScope): Listing<Repo> {

        val database = AppDatabase.instance

        val dataSourceFactory = database.repoDao().getAll()

        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, pagingConfig)

        val boundaryCallback = RepoBoundaryCallback(scope, database, pagingConfig)
        livePagedListBuilder.setBoundaryCallback(boundaryCallback)

        val pagedList = livePagedListBuilder.build()

        return Listing(
            pagedList = pagedList
        )
    }

}