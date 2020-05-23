package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.SearchUsersQuery
import com.gianlucaparadise.githubbrowser.fragment.UserFragment

data class User(
    /**
     * The username used to login.
     */
    val login: String,
    /**
     * The user's public profile bio.
     */
    val bio: String?,
    /**
     * The user's public profile name.
     */
    val name: String?,
    /**
     * A URL pointing to the user's public avatar.
     */
    val avatarUrl: Any,
    /**
     * The total count of users the given user is followed by.
     */
    val followersCount: Int,
    /**
     * The total count of users the given user is following.
     */
    val followingCount: Int
) {
    val displayName: String
        get() = name ?: login

    companion object {
        fun fromUserFragment(userFragment: UserFragment?): User? {
            if (userFragment == null) return null

            return User(
                login = userFragment.login,
                bio = userFragment.bio,
                name = userFragment.name,
                avatarUrl = userFragment.avatarUrl,
                followersCount = userFragment.followers.totalCount,
                followingCount = userFragment.following.totalCount
            )
        }

        private fun fromSearchUsersNodeList(searchUserNodes: List<SearchUsersQuery.Node?>?): List<User>? {
            if (searchUserNodes == null) return null

            val users = mutableListOf<User>()

            searchUserNodes.forEach { node ->
                val user = User.fromUserFragment(node?.fragments?.userFragment)
                if (user != null) {
                    users.add(user)
                }
            }

            return users
        }

        fun fromSearchUsersResponse(users: SearchUsersQuery.Search?): PaginatedResponse<User>? {
            if (users == null) return null

            return PaginatedResponse(
                endCursor = users.pageInfo.endCursor,
                hasNextPage = users.pageInfo.hasNextPage,
                totalCount = users.userCount,
                nodes = User.fromSearchUsersNodeList(users.nodes)
            )
        }
    }
}