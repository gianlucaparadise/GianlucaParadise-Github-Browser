package com.gianlucaparadise.githubbrowser.data

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.gianlucaparadise.githubbrowser.network.BackendService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RepositoryDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<String, Repository>() {
    companion object {
        const val tag = "RepositoryDataSource"
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Repository>
    ) {
        scope.launch {
            try {
                Log.d(
                    tag, "Loading initial, start - " +
                            "pagesize: ${params.requestedLoadSize} "
                )
                val response =
                    BackendService.retrieveAuthenticatedUserRepositories(
                        first = params.requestedLoadSize
                    )
                Log.d(
                    tag, "Loading initial, response - " +
                            "size: ${response.nodes.size} " +
                            "first: ${response.nodes.first().name}"
                )
                callback.onResult(response.nodes, null, response.endCursor)
            } catch (e: Exception) {
                Log.e(tag, "Error loadInitial", e)
                // callback.onError(e)
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Repository>) {
        scope.launch {
            try {
                Log.d(
                    tag, "Loading after, start - " +
                            "page: ${params.key} " +
                            "pagesize: ${params.requestedLoadSize} "
                )

                val response = BackendService.retrieveAuthenticatedUserRepositories(
                    first = params.requestedLoadSize,
                    startCursor = params.key
                )

                // When I reach the last page, I set key to null to stop paging
                val nextKey =
                    if (response.hasNextPage) response.endCursor else null
                Log.d(
                    tag, "Loading after, response - " +
                            "size: ${response.nodes.size} " +
                            "first: ${response.nodes.firstOrNull()?.name} " +
                            "nextKey: $nextKey"
                )
                callback.onResult(response.nodes, nextKey)
            } catch (e: Exception) {
                Log.e(tag, "Error loadAfter", e)
                // callback.onError(e)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Repository>
    ) {
        Log.d(tag, "Load Before") // Not needed
    }

}