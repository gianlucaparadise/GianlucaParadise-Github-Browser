package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import kotlinx.coroutines.CoroutineScope

class SearchUserResultsDataSource(scope: CoroutineScope, searchQuery: String? = null) :
    SearchableDataSource<User>(scope, searchQuery) {

    override suspend fun load(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<User> {
        return BackendService.searchUsers(query ?: "", first, startCursor)
    }

}