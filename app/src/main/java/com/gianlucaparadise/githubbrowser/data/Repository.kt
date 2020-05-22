package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.AuthenticatedUserRepositoriesQuery
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

        private fun fromRepositoryNodeList(repositoryNodes: List<AuthenticatedUserRepositoriesQuery.Node?>?): Array<Repository?>? {
            if (repositoryNodes == null) return null

            val repositories = arrayOfNulls<Repository>(repositoryNodes.count())

            repositoryNodes.forEachIndexed { index, node ->
                repositories[index] = Repository.fromRepositoryFragment(node?.fragments?.repositoryFragment)
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

        fun fromRepositoriesReponse(repositories: AuthenticatedUserRepositoriesQuery.Repositories?): PaginatedResponse<Repository>? {
            if (repositories == null) return null

            return PaginatedResponse(
                endCursor = repositories.pageInfo.endCursor,
                hasNextPage = repositories.pageInfo.hasNextPage,
                totalCount = repositories.totalCount,
                nodes = Repository.fromRepositoryNodeList(repositories.nodes)
            )
        }
    }
}