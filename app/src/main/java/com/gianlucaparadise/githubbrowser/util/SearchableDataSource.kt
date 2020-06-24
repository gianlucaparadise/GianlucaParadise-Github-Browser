package com.gianlucaparadise.githubbrowser.util

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gianlucaparadise.githubbrowser.inMemory.InMemoryDao
import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class SearchableDataSource<T>(
    private val scope: CoroutineScope,
    private val searchQuery: String?,
    private val inMemoryDao: InMemoryDao<T>? = null
) :
    PageKeyedDataSource<String, T>() {
    abstract val tag: String

    init {
        inMemoryDao?.onItemUpdated = {
            Log.d(tag, "Item Updated, invalidating DataSource")
            invalidate() // this invalidate will re-create the datasource
        }
    }

    var networkListener: NetworkListener? = null

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, T>
    ) {
        val items = inMemoryDao?.getAll()
        if (items?.any() == true) {
            val lastItem = items.lastOrNull()
            val nextKey = if (lastItem == null) null else getNextKey(lastItem)

            Log.d(
                tag, "Loading initial, snapshot - " +
                        "pagesize: ${items.size} " +
                        "query: $searchQuery " +
                        "nextKey: $nextKey"
            )

            callback.onResult(items, null, nextKey)
            return
        }

        scope.launch {
            try {
                networkListener?.onLoading()

                Log.d(
                    tag, "Loading initial, start - " +
                            "pagesize: ${params.requestedLoadSize} " +
                            "query: $searchQuery"
                )

                val response = loadInitial(
                    first = params.requestedLoadSize,
                    query = searchQuery
                )

                Log.d(
                    tag, "Loading initial, response - " +
                            "size: ${response.nodes.size} " +
                            "first: ${response.nodes.firstOrNull()}"
                )

                inMemoryDao?.loadInitial(response.nodes)
                callback.onResult(response.nodes, null, response.endCursor)

                networkListener?.onLoaded()

            } catch (e: Exception) {
                Log.e(tag, "Error loadInitial", e)
                networkListener?.onError("Error loadInitial", e)
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, T>) {
        scope.launch {
            try {
                networkListener?.onLoading()

                Log.d(
                    tag, "Loading after, start - " +
                            "page: ${params.key} " +
                            "pagesize: ${params.requestedLoadSize} " +
                            "query: $searchQuery"
                )

                val response = loadAfter(
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

                inMemoryDao?.append(response.nodes)
                callback.onResult(response.nodes, nextKey)

                networkListener?.onLoaded()

            } catch (e: Exception) {
                Log.e(tag, "Error loadAfter", e)
                networkListener?.onError("Error loadAfter", e)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, T>
    ) {
        Log.d(tag, "Load Before") // Not needed
    }

    abstract suspend fun loadInitial(
        first: Int,
        query: String?
    ): PaginatedResponse<T>

    abstract suspend fun loadAfter(
        first: Int,
        startCursor: String?,
        query: String?
    ): PaginatedResponse<T>

    /**
     * Get pagination cursor from item
     * When the items are loaded from snapshot, it is needed to get the next pagination cursor to
     * retrieve the next page from backend
     */
    abstract fun getNextKey(item: T): String?

    /**
     * Callbacks for network state changes
     */
    interface NetworkListener {
        /**
         * Called when a network call is in progress
         */
        fun onLoading()

        /**
         * Called when a network call finished
         */
        fun onLoaded()

        /**
         * Called when a network failed
         */
        fun onError(message: String, ex: Throwable)
    }

    abstract class Factory<T, DATASOURCE : SearchableDataSource<T>> :
        DataSource.Factory<String, T>() {

        private var source: DATASOURCE? = null
            set(value) {
                // when source changes I stop listening the old instance and start listening the new one
                field?.networkListener = null
                value?.networkListener = this.networkListener

                field = value
            }

        private var query: String? = null
        /**
         * Listen to network state changes
         */
        var networkListener: NetworkListener? = null

        fun updateSearchQuery(query: String) {
            if (query == this.query) return // Nothing changed, nothing to do

            this.query = query
            source?.invalidate() // this invalidate will re-create the datasource
        }

        override fun create(): DataSource<String, T> {
            val source = create(query)
            this.source = source
            return source
        }

        abstract fun create(query: String?): DATASOURCE
    }
}