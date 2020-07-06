package com.gianlucaparadise.githubbrowser.inMemory

import com.gianlucaparadise.githubbrowser.vo.Repo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * As per Android Docs suggestion:
 * "If you have more granular update signals, such as a network API signaling an update to a single
 * item in the list, it's recommended to load data from network into memory. Then present that data
 * to the PagedList via a DataSource that wraps an in-memory snapshot. Each time the in-memory copy
 * changes, invalidate the previous DataSource, and a new one wrapping the new state of the
 * snapshot can be created."
 */
@Singleton
class AppInMemorySnapshot @Inject constructor() {

    val repoDao: InMemoryDao<Repo> by lazy {
        InMemoryDao<Repo>()
    }
}