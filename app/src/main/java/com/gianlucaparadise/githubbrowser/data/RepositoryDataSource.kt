package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.util.SearchableDataSource
import kotlinx.coroutines.CoroutineScope

class RepositoryDataSource(scope: CoroutineScope) : SearchableDataSource<Repository>(scope) {

        return BackendService.retrieveAuthenticatedUserRepositories(first, startCursor)
    }

}