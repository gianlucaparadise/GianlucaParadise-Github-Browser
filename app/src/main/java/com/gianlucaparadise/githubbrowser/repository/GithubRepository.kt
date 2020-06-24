package com.gianlucaparadise.githubbrowser.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gianlucaparadise.githubbrowser.datasource.RepoBoundaryCallback
import com.gianlucaparadise.githubbrowser.datasource.SearchRepoResultsDataSource
import com.gianlucaparadise.githubbrowser.datasource.SearchUserResultsDataSource
import com.gianlucaparadise.githubbrowser.db.AppDatabase
import com.gianlucaparadise.githubbrowser.inMemory.AppInMemorySnapshot
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import com.gianlucaparadise.githubbrowser.vo.Repo
import com.gianlucaparadise.githubbrowser.vo.User
import kotlinx.coroutines.CoroutineScope

/**
 * This is a mediator object between the InMemory snapshots for the search results and
 * the DataBase snapshot for the authenticated repos
 */
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
            pagedList = pagedList,
            networkState = boundaryCallback.networkState
        )
    }

    fun searchRepos(scope: CoroutineScope): SearchableListing<Repo> {

        val dataSourceFactory =
            object : SearchableDataSource.Factory<Repo, SearchRepoResultsDataSource>() {
                override fun create(query: String?) = SearchRepoResultsDataSource(scope, query)
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
                override fun create(query: String?) = SearchUserResultsDataSource(scope, query)
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
        AppDatabase.instance.repoDao().update(repo)
        AppInMemorySnapshot.instance.repoDao.update(repo)
    }
}