package com.gianlucaparadise.githubbrowser.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gianlucaparadise.githubbrowser.datasource.RepoBoundaryCallback
import com.gianlucaparadise.githubbrowser.datasource.SearchRepoResultsDataSource
import com.gianlucaparadise.githubbrowser.datasource.SearchUserResultsDataSource
import com.gianlucaparadise.githubbrowser.db.AppDatabase
import com.gianlucaparadise.githubbrowser.inMemory.AppInMemorySnapshot
import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.vo.User
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is a mediator object between the InMemory snapshots for the search results and
 * the DataBase snapshot for the authenticated repos
 */
@Singleton
class GithubRepository @Inject constructor(private val database: AppDatabase, private val backend: BackendService) {

    private val pagingConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setInitialLoadSizeHint(15)
        .setEnablePlaceholders(false)
        .build()

    fun retrieveAuthenticatedUserRepos(scope: CoroutineScope): Listing<Repo> {

        val dataSourceFactory = database.repoDao().getAll()

        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, pagingConfig)

        val boundaryCallback = RepoBoundaryCallback(scope, database, backend, pagingConfig)
        livePagedListBuilder.setBoundaryCallback(boundaryCallback)

        val pagedList = livePagedListBuilder.build()

        return Listing(
            pagedList = pagedList,
            networkState = boundaryCallback.networkState
        )
    }

    fun searchRepos(scope: CoroutineScope): SearchableListing<Repo> {

        val dataSourceFactory =
            object : SearchableDataSource.Factory<Repo, SearchRepoResultsDataSource>() {
                override fun create(query: String?) = SearchRepoResultsDataSource(scope, backend, query)
            }

        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, pagingConfig)

        val pagedList = livePagedListBuilder.build()

        return SearchableListing(
            pagedList = pagedList,
            search = {
                dataSourceFactory.updateSearchQuery(it)
            },
            networkState = createStatusLiveData(dataSourceFactory)
        )
    }

    fun searchUsers(scope: CoroutineScope): SearchableListing<User> {

        val dataSourceFactory =
            object : SearchableDataSource.Factory<User, SearchUserResultsDataSource>() {
                override fun create(query: String?) = SearchUserResultsDataSource(scope, backend, query)
            }

        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, pagingConfig)

        val pagedList = livePagedListBuilder.build()

        return SearchableListing(
            pagedList = pagedList,
            search = {
                dataSourceFactory.updateSearchQuery(it)
            },
            networkState = createStatusLiveData(dataSourceFactory)
        )
    }

    private fun createStatusLiveData(dataSourceFactory: SearchableDataSource.Factory<*, *>): MutableLiveData<NetworkState> {
        val liveData = MutableLiveData<NetworkState>()
        dataSourceFactory.networkListener = object : SearchableDataSource.NetworkListener {
            override fun onLoading() {
                liveData.postValue(NetworkState.LOADING)
            }

            override fun onLoaded() {
                liveData.postValue(NetworkState.LOADED)
            }

            override fun onError(message: String, ex: Throwable) {
                val state = NetworkState.error(message)
                liveData.postValue(state)
            }

        }
        return liveData
    }

    suspend fun updateRepo(repo: Repo) {
        database.repoDao().update(repo)
        AppInMemorySnapshot.instance.repoDao.update(repo)
    }
}