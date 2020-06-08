package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import com.gianlucaparadise.githubbrowser.vo.Repo
import kotlinx.coroutines.CoroutineScope

class RepoDataSource(scope: CoroutineScope, searchQuery: String? = null) :
    SearchableDataSource<Repo>(scope, searchQuery) {

    override suspend fun load(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<Repo> {
        return BackendService.retrieveAuthenticatedUserRepos(first, startCursor)
    }

}