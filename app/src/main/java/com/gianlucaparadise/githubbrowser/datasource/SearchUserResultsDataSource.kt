package com.gianlucaparadise.githubbrowser.datasource

import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import com.gianlucaparadise.githubbrowser.vo.User
import kotlinx.coroutines.CoroutineScope

class SearchUserResultsDataSource(scope: CoroutineScope, searchQuery: String? = null) :
    SearchableDataSource<User>(scope, searchQuery) {

    companion object {
        const val tag = "SearchUserResultsDataSource"
    }

    override val tag: String
        get() = SearchUserResultsDataSource.tag

    override suspend fun loadInitial(
        first: Int,
        query: String?
    ): PaginatedResponse<User> {
        return BackendService.searchUsers(query ?: "", first)
    }

    override suspend fun loadAfter(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<User> {
        return BackendService.searchUsers(query ?: "", first, startCursor)
    }

    override fun getNextKey(item: User): String? = null // Users are never loaded from snapshot
}