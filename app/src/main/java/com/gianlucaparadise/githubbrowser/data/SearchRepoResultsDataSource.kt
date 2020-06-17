package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.inMemory.AppInMemorySnapshot
import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import com.gianlucaparadise.githubbrowser.vo.Repo
import kotlinx.coroutines.CoroutineScope

class SearchRepoResultsDataSource(scope: CoroutineScope, searchQuery: String? = null) :
    SearchableDataSource<Repo>(scope, searchQuery) {

    companion object {
        const val tag = "SearchRepoResultsDataSource"
    }

    override val tag: String
        get() = SearchRepoResultsDataSource.tag

    override suspend fun loadInitial(
        first: Int,
        query: String?
    ): PaginatedResponse<Repo> {
        val response = BackendService.searchRepos(query ?: "", first)
        AppInMemorySnapshot.instance.repoDao.loadInitial(response.nodes)
        return response
    }

    override suspend fun loadAfter(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<Repo> {
        val response = BackendService.searchRepos(query ?: "", first, startCursor)
        AppInMemorySnapshot.instance.repoDao.append(response.nodes)
        return response
    }

}