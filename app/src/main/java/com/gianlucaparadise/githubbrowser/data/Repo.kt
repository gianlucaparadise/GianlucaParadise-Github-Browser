package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.AuthenticatedUserRepositoriesQuery
import com.gianlucaparadise.githubbrowser.SearchRepositoriesQuery
import com.gianlucaparadise.githubbrowser.fragment.RepositoryFragment
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

        private fun fromRepoNodeList(repositoryNodes: List<AuthenticatedUserRepositoriesQuery.Node?>?): List<Repo> {
            if (repositoryNodes == null) return emptyList()

            val repositories = mutableListOf<Repo>()

            repositoryNodes.forEach { node ->
                val repository =
                    Repo.fromRepoFragment(node?.fragments?.repositoryFragment)

                if (repository != null) {
                    repositories.add(repository)
                }
            }

            return repositories
        }

        private fun fromRepoFragment(repositoryFragment: RepositoryFragment?): Repo? {
            if (repositoryFragment == null) return null

            return Repo(
                id = repositoryFragment.id,
                name = repositoryFragment.name,
                description = repositoryFragment.description,
                primaryLanguageName = repositoryFragment.primaryLanguage?.name,
                stargazersCount = repositoryFragment.stargazers.totalCount,
                owner = User.fromUserFragment(repositoryFragment.owner.fragments.userFragment),
                viewerHasStarred = repositoryFragment.viewerHasStarred
            )
        }

        private fun fromSearchReposNodeList(searchRepositoriesNodes: List<SearchRepositoriesQuery.Node?>?): List<Repo> {
            if (searchRepositoriesNodes == null) return emptyList()

            val repositories = mutableListOf<Repo>()

            searchRepositoriesNodes.forEach { node ->
                val repository =
                    Repo.fromRepoFragment(node?.fragments?.repositoryFragment)
                if (repository != null) {
                    repositories.add(repository)
                }
            }

            return repositories
        }

        fun fromReposResponse(repositories: AuthenticatedUserRepositoriesQuery.Repositories): PaginatedResponse<Repo> {
            return PaginatedResponse(
                endCursor = repositories.pageInfo.endCursor,
                hasNextPage = repositories.pageInfo.hasNextPage,
                totalCount = repositories.totalCount,
                nodes = Repo.fromRepoNodeList(repositories.nodes)
            )
        }

        fun fromSearchReposResponse(repositories: SearchRepositoriesQuery.Search): PaginatedResponse<Repo> {
            return PaginatedResponse(
                endCursor = repositories.pageInfo.endCursor,
                hasNextPage = repositories.pageInfo.hasNextPage,
                totalCount = repositories.repositoryCount,
                nodes = Repo.fromSearchReposNodeList(repositories.nodes)
            )
        }
    }
}