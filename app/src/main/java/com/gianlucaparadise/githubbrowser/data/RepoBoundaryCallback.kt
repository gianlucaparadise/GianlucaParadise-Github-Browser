package com.gianlucaparadise.githubbrowser.data

import android.util.Log
import com.gianlucaparadise.githubbrowser.util.PagingRequestHelper
import androidx.paging.PagedList
import com.gianlucaparadise.githubbrowser.db.AppDatabase
import com.gianlucaparadise.githubbrowser.network.BackendService
import com.gianlucaparadise.githubbrowser.vo.Repo
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.Executors

class RepoBoundaryCallback(
    private val scope: CoroutineScope,
    private val db: AppDatabase,
    private val pageConfig: PagedList.Config
) :
    PagedList.BoundaryCallback<Repo>() {

    companion object {
        const val tag = "BoundaryCallback"
    }

    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)

    private var lastResponseKey: String? = null

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
            scope.launch(executor.asCoroutineDispatcher()) {
                try {

                    Log.d(
                        tag, "Loading initial, start - " +
                                "page: $lastResponseKey " +
                                "pagesize: ${pageConfig.pageSize} "
                    )

                    val response =
                        BackendService.retrieveAuthenticatedUserRepos(
                            pageConfig.pageSize,
                            lastResponseKey
                        )
                    lastResponseKey = response.endCursor

                    Log.d(
                        tag, "Loading initial, response - " +
                                "size: ${response.nodes.size} " +
                                "first: ${response.nodes.firstOrNull()}"
                    )

                    val repos = response.nodes
                    db.repoDao().insert(repos)

                    helperCallback.recordSuccess()

                } catch (e: Exception) {

                    Log.e(tag, "Error onZeroItemsLoaded", e)
                    helperCallback.recordFailure(e)
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        super.onItemAtEndLoaded(itemAtEnd)

        if (lastResponseKey == null) {
            Log.d(tag, "End Loading")
            return
        }

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
            scope.launch(executor.asCoroutineDispatcher()) {
                try {

                    Log.d(
                        tag, "Loading after, start - " +
                                "page: $lastResponseKey " +
                                "pagesize: ${pageConfig.pageSize} "
                    )

                    val response =
                        BackendService.retrieveAuthenticatedUserRepos(
                            pageConfig.pageSize,
                            lastResponseKey
                        )
                    lastResponseKey = response.endCursor

                    Log.d(
                        tag, "Loading after, response - " +
                                "size: ${response.nodes.size} " +
                                "first: ${response.nodes.firstOrNull()} " +
                                "nextKey: $lastResponseKey"
                    )

                    val repos = response.nodes
                    db.repoDao().insert(repos)

                    helperCallback.recordSuccess()

                } catch (e: Exception) {

                    Log.e(tag, "Error onItemAtEndLoaded", e)
                    helperCallback.recordFailure(e)
                }
            }
        }
    }
}