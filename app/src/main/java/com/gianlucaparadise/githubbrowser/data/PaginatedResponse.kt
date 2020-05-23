package com.gianlucaparadise.githubbrowser.data

data class PaginatedResponse<T>(
    /**
     * When paginating forwards, the cursor to continue.
     */
    val endCursor: String?,
    /**
     * When paginating forwards, are there more items?
     */
    val hasNextPage: Boolean,
    /**
     * Identifies the total count of items in the connection.
     */
    val totalCount: Int,
    /**
     * A list of nodes.
     */
    val nodes: List<T>?
)