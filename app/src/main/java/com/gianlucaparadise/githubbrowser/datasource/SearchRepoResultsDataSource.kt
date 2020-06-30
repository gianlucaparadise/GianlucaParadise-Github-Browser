package com.gianlucaparadise.githubbrowser.datasource

import com.gianlucaparadise.githubbrowser.inMemory.AppInMemorySnapshot
import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import com.gianlucaparadise.githubbrowser.vo.Repo
import kotlinx.coroutines.CoroutineScope

class SearchRepoResultsDataSource(scope: CoroutineScope, private val backend: BackendService, searchQuery: String? = null) :
    SearchableDataSource<Repo>(scope, searchQuery, AppInMemorySnapshot.instance.repoDao) {

    companion object {
        const val tag = "SearchRepoResultsDataSource"
    }

    override val tag: String
        get() = SearchRepoResultsDataSource.tag

    override suspend fun loadInitial(
        first: Int,
        query: String?
    ): PaginatedResponse<Repo> {
        return backend.searchRepos(query ?: "", first)
    }

    override suspend fun loadAfter(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<Repo> {
        return backend.searchRepos(query ?: "", first, startCursor)
    }

    override fun getNextKey(item: Repo): String? = item.paginationCursor
}