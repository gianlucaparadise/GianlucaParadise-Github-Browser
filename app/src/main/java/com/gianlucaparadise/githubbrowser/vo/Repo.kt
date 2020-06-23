package com.gianlucaparadise.githubbrowser.vo

import androidx.room.*
import com.gianlucaparadise.githubbrowser.AuthenticatedUserReposQuery
import com.gianlucaparadise.githubbrowser.SearchReposQuery
import com.gianlucaparadise.githubbrowser.fragment.RepoFragment
import java.io.Serializable

@Entity
data class Repo(
    @PrimaryKey val id: String,
    /**
     * The name of the repo.
     */
    @ColumnInfo(name = "name") val name: String,
    /**
     * The description of the repo.
     */
    @ColumnInfo(name = "description") val description: String?,
    /**
     * The primary language name of the repo's code.
     */
    @ColumnInfo(name = "primaryLanguageName") val primaryLanguageName: String?,
    /**
     * A list of users who have starred this starrable.
     */
    @ColumnInfo(name = "stargazersCount") val stargazersCount: Int,
    /**
     * The User owner of the repo.
     */
    @Embedded(prefix = "owner_") val owner: User?,
    /**
     * Returns a boolean indicating whether the viewing user has starred this starrable.
     */
    @ColumnInfo(name = "viewerHasStarred") val viewerHasStarred: Boolean,

    /**
     * When this is the last item of a page, this prop contains the cursor of this item. Otherwise is null.
     */
    @ColumnInfo(name = "paginationCursor") var paginationCursor: String?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Repo) return false

        return other.id == this.id
    }

    companion object {

        private fun fromRepoNodeList(
            repoNodes: List<AuthenticatedUserReposQuery.Node?>?,
            lastItemCursor: String?
        ): List<Repo> {
            if (repoNodes == null) return emptyList()

            val repos = mutableListOf<Repo>()

            repoNodes.forEach { node ->
                val repo =
                    fromRepoFragment(node?.fragments?.repoFragment)

                if (repo != null) {
                    repos.add(repo)
                }
            }

            repos.lastOrNull()?.paginationCursor = lastItemCursor

            return repos
        }

        private fun fromRepoFragment(repoFragment: RepoFragment?): Repo? {
            if (repoFragment == null) return null

            return Repo(
                id = repoFragment.id,
                name = repoFragment.name,
                description = repoFragment.description,
                primaryLanguageName = repoFragment.primaryLanguage?.name,
                stargazersCount = repoFragment.stargazers.totalCount,
                owner = User.fromUserFragment(
                    repoFragment.owner.fragments.userFragment
                ),
                viewerHasStarred = repoFragment.viewerHasStarred,
                paginationCursor = null // this will be evaluated in a second place
            )
        }

        private fun fromSearchReposNodeList(
            searchReposNodes: List<SearchReposQuery.Node?>?,
            lastItemCursor: String?
        ): List<Repo> {
            if (searchReposNodes == null) return emptyList()

            val repos = mutableListOf<Repo>()

            searchReposNodes.forEachIndexed { i, node ->
                val repo =
                    fromRepoFragment(node?.fragments?.repoFragment)
                if (repo != null) {
                    repos.add(repo)
                }
            }

            repos.lastOrNull()?.paginationCursor = lastItemCursor

            return repos
        }

        fun fromReposResponse(repos: AuthenticatedUserReposQuery.Repositories): PaginatedResponse<Repo> {
            return PaginatedResponse(
                endCursor = repos.pageInfo.endCursor,
                hasNextPage = repos.pageInfo.hasNextPage,
                totalCount = repos.totalCount,
                nodes = fromRepoNodeList(repos.nodes, repos.pageInfo.endCursor)
            )
        }

        fun fromSearchReposResponse(repos: SearchReposQuery.Search): PaginatedResponse<Repo> {
            return PaginatedResponse(
                endCursor = repos.pageInfo.endCursor,
                hasNextPage = repos.pageInfo.hasNextPage,
                totalCount = repos.repositoryCount,
                nodes = fromSearchReposNodeList(repos.nodes, repos.pageInfo.endCursor)
            )
        }
    }
}