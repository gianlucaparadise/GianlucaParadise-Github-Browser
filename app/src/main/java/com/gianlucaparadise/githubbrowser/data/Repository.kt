package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.AuthenticatedUserRepositoriesQuery
import com.gianlucaparadise.githubbrowser.SearchRepositoriesQuery
import com.gianlucaparadise.githubbrowser.fragment.RepositoryFragment

data class Repository(
    /**
     * The name of the repository.
     */
    val name: String,
    /**
     * The description of the repository.
     */
    val description: String?,
    /**
     * A description of the repository, rendered to HTML without any links in it.
     */
    val shortDescriptionHTML: Any,
    /**
     * The primary language name of the repository's code.
     */
    val primaryLanguageName: String?,
    /**
     * A list of users who have starred this starrable.
     */
    val stargazersCount: Int,
    /**
     * The User owner of the repository.
     */
    val owner: User?
) {
    companion object {

        private fun fromRepositoryNodeList(repositoryNodes: List<AuthenticatedUserRepositoriesQuery.Node?>?): List<Repository> {
            if (repositoryNodes == null) return emptyList()

            val repositories = mutableListOf<Repository>()

            repositoryNodes.forEach { node ->
                val repository =
                    Repository.fromRepositoryFragment(node?.fragments?.repositoryFragment)

                if (repository != null) {
                    repositories.add(repository)
                }
            }

            return repositories
        }

        private fun fromRepositoryFragment(repositoryFragment: RepositoryFragment?): Repository? {
            if (repositoryFragment == null) return null

            return Repository(
                name = repositoryFragment.name,
                description = repositoryFragment.description,
                shortDescriptionHTML = repositoryFragment.shortDescriptionHTML,
                primaryLanguageName = repositoryFragment.primaryLanguage?.name,
                stargazersCount = repositoryFragment.stargazers.totalCount,
                owner = User.fromUserFragment(repositoryFragment.owner.fragments.userFragment)
            )
        }

        private fun fromSearchRepositoriesNodeList(searchRepositoriesNodes: List<SearchRepositoriesQuery.Node?>?): List<Repository> {
            if (searchRepositoriesNodes == null) return emptyList()

            val repositories = mutableListOf<Repository>()

            searchRepositoriesNodes.forEach { node ->
                val repository = Repository.fromRepositoryFragment(node?.fragments?.repositoryFragment)
                if (repository != null) {
                    repositories.add(repository)
                }
            }

            return repositories
        }

        fun fromRepositoriesResponse(repositories: AuthenticatedUserRepositoriesQuery.Repositories): PaginatedResponse<Repository> {
            return PaginatedResponse(
                endCursor = repositories.pageInfo.endCursor,
                hasNextPage = repositories.pageInfo.hasNextPage,
                totalCount = repositories.totalCount,
                nodes = Repository.fromRepositoryNodeList(repositories.nodes)
            )
        }

        fun fromSearchRepositoriesResponse(repositories: SearchRepositoriesQuery.Search): PaginatedResponse<Repository> {
            return PaginatedResponse(
                endCursor = repositories.pageInfo.endCursor,
                hasNextPage = repositories.pageInfo.hasNextPage,
                totalCount = repositories.repositoryCount,
                nodes = Repository.fromSearchRepositoriesNodeList(repositories.nodes)
            )
        }
    }
}