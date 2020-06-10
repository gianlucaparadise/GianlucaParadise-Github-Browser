package com.gianlucaparadise.githubbrowser.data

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

    override suspend fun load(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<Repo> {
        return BackendService.searchRepos(query ?: "", first, startCursor)
    }

}