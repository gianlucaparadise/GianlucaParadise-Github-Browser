package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import kotlinx.coroutines.CoroutineScope

class SearchRepositoryResultsDataSource(scope: CoroutineScope, searchQuery: String? = null) :
    SearchableDataSource<Repo>(scope, searchQuery) {

    override suspend fun load(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<Repo> {
        return BackendService.searchRepos(query ?: "", first, startCursor)
    }

}