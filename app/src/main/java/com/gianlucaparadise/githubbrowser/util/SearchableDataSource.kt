package com.gianlucaparadise.githubbrowser.util

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class SearchableDataSource<T>(
    private val scope: CoroutineScope,
    var searchQuery: String? = null
) :
    PageKeyedDataSource<String, T>() {
    abstract val tag: String

    private val searchQueryNullable
        get() = if (searchQuery.isNullOrBlank()) null else searchQuery

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, T>
    ) {
        scope.launch {
            try {
                Log.d(
                    tag, "Loading initial, start - " +
                            "pagesize: ${params.requestedLoadSize} " +
                            "query: $searchQueryNullable"
                )

                val response = load(
                    first = params.requestedLoadSize,
                    startCursor = null,
                    query = searchQueryNullable
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
                            "query: $searchQueryNullable"
                )

                val response = load(
                    first = params.requestedLoadSize,
                    startCursor = params.key,
                    query = searchQueryNullable
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

    fun updateSearchQuery(query: String) {
        if (query == searchQuery) return // Nothing changed, nothing to do

        Log.d(tag, "Changed Query: $query")
        searchQuery = query
        super.invalidate()
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }

    abstract suspend fun load(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<T>
}