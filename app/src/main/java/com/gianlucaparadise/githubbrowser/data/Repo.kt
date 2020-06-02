package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.AuthenticatedUserReposQuery
import com.gianlucaparadise.githubbrowser.SearchReposQuery
import com.gianlucaparadise.githubbrowser.fragment.RepoFragment
import java.io.Serializable

data class Repo(
    val id: String,
    /**
     * The name of the repo.
     */
    val name: String,
    /**
     * The description of the repo.
     */
    val description: String?,
    /**
     * The primary language name of the repo's code.
     */
    val primaryLanguageName: String?,
    /**
     * A list of users who have starred this starrable.
     */
    val stargazersCount: Int,
    /**
     * The User owner of the repo.
     */
    val owner: User?,
    /**
     * Returns a boolean indicating whether the viewing user has starred this starrable.
     */
    val viewerHasStarred: Boolean
) : Serializable {
    companion object {

        private fun fromRepoNodeList(repoNodes: List<AuthenticatedUserReposQuery.Node?>?): List<Repo> {
            if (repoNodes == null) return emptyList()

            val repos = mutableListOf<Repo>()

            repoNodes.forEach { node ->
                val repo =
                    Repo.fromRepoFragment(node?.fragments?.repoFragment)

                if (repo != null) {
                    repos.add(repo)
                }
            }

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
                owner = User.fromUserFragment(repoFragment.owner.fragments.userFragment),
                viewerHasStarred = repoFragment.viewerHasStarred
            )
        }

        private fun fromSearchReposNodeList(searchReposNodes: List<SearchReposQuery.Node?>?): List<Repo> {
            if (searchReposNodes == null) return emptyList()

            val repos = mutableListOf<Repo>()

            searchReposNodes.forEach { node ->
                val repo =
                    Repo.fromRepoFragment(node?.fragments?.repoFragment)
                if (repo != null) {
                    repos.add(repo)
                }
            }

            return repos
        }

        fun fromReposResponse(repos: AuthenticatedUserReposQuery.Repositories): PaginatedResponse<Repo> {
            return PaginatedResponse(
                endCursor = repos.pageInfo.endCursor,
                hasNextPage = repos.pageInfo.hasNextPage,
                totalCount = repos.totalCount,
                nodes = Repo.fromRepoNodeList(repos.nodes)
            )
        }

        fun fromSearchReposResponse(repos: SearchReposQuery.Search): PaginatedResponse<Repo> {
            return PaginatedResponse(
                endCursor = repos.pageInfo.endCursor,
                hasNextPage = repos.pageInfo.hasNextPage,
                totalCount = repos.repositoryCount,
                nodes = Repo.fromSearchReposNodeList(repos.nodes)
            )
        }
    }
}