package com.gianlucaparadise.githubbrowser.util

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.gianlucaparadise.githubbrowser.data.PaginatedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class SearchableDataSource<T>(private val scope: CoroutineScope) :
    PageKeyedDataSource<String, T>() {
    companion object {
        const val tag = "SearchableDataSource"
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, T>
    ) {
        scope.launch {
            try {
                Log.d(
                    tag, "Loading initial, start - " +
                            "pagesize: ${params.requestedLoadSize} "
                )

                val response = load(first = params.requestedLoadSize, startCursor = null)

                Log.d(
                    tag, "Loading initial, response - " +
                            "size: ${response.nodes.size} " +
                            "first: ${response.nodes.first()}"
                )

                callback.onResult(response.nodes, null, response.endCursor)

            } catch (e: Exception) {
                Log.e(tag, "Error loadInitial", e)
                // callback.onError(e)
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, T>) {
        scope.launch {
            try {
                Log.d(
                    tag, "Loading after, start - " +
                            "page: ${params.key} " +
                            "pagesize: ${params.requestedLoadSize} "
                )

                val response = load(first = params.requestedLoadSize, startCursor = params.key)

                // When I reach the last page, I set key to null to stop paging
                val nextKey = if (response.hasNextPage) response.endCursor else null

                Log.d(
                    tag, "Loading after, response - " +
                            "size: ${response.nodes.size} " +
                            "first: ${response.nodes.firstOrNull()} " +
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
        callback: LoadCallback<String, T>
    ) {
        Log.d(tag, "Load Before") // Not needed
    }

    abstract suspend fun load(first: Int, startCursor: String?): PaginatedResponse<T>
}