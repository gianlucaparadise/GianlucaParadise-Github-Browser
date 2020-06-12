package com.gianlucaparadise.githubbrowser.util

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class SearchableDataSource<T>(
    private val scope: CoroutineScope,
    val searchQuery: String?
) :
    PageKeyedDataSource<String, T>() {
    abstract val tag: String

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, T>
    ) {
        scope.launch {
            try {
                Log.d(
                    tag, "Loading initial, start - " +
                            "pagesize: ${params.requestedLoadSize} " +
                            "query: $searchQuery"
                )

                val response = load(
                    first = params.requestedLoadSize,
                    startCursor = null,
                    query = searchQuery
                )

                Log.d(
                    tag, "Loading initial, response - " +
                            "size: ${response.nodes.size} " +
                            "first: ${response.nodes.firstOrNull()}"
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
                            "pagesize: ${params.requestedLoadSize} " +
                            "query: $searchQuery"
                )

                val response = load(
                    first = params.requestedLoadSize,
                    startCursor = params.key,
                    query = searchQuery
                )

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

    abstract suspend fun load(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<T>
}