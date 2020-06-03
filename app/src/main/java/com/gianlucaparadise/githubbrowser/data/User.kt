package com.gianlucaparadise.githubbrowser.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gianlucaparadise.githubbrowser.SearchUsersQuery
import com.gianlucaparadise.githubbrowser.fragment.UserFragment
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey val id: String,
    /**
     * The username used to login.
     */
    @ColumnInfo(name = "login") val login: String,
    /**
     * The user's public profile bio.
     */
    @ColumnInfo(name = "bio") val bio: String?,
    /**
     * The user's public profile name.
     */
    @ColumnInfo(name = "name") val name: String?,
    /**
     * A URL pointing to the user's public avatar.
     */
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String,
    /**
     * The total count of users the given user is followed by.
     */
    @ColumnInfo(name = "followersCount") val followersCount: Int,
    /**
     * The total count of users the given user is following.
     */
    @ColumnInfo(name = "followingCount") val followingCount: Int
) : Serializable {
    val displayName: String
        get() = name ?: login

    companion object {
        fun fromUserFragment(userFragment: UserFragment?): User? {
            if (userFragment == null) return null

            return User(
                id = userFragment.id,
                login = userFragment.login,
                bio = userFragment.bio,
                name = userFragment.name,
                avatarUrl = userFragment.avatarUrl.toString(),
                followersCount = userFragment.followers.totalCount,
                followingCount = userFragment.following.totalCount
            )
        }

        private fun fromSearchUsersNodeList(searchUserNodes: List<SearchUsersQuery.Node?>?): List<User> {
            if (searchUserNodes == null) return emptyList()

            val users = mutableListOf<User>()

            searchUserNodes.forEach { node ->
                val user = User.fromUserFragment(node?.fragments?.userFragment)
                if (user != null) {
                    users.add(user)
                }
            }

            return users
        }

        fun fromSearchUsersResponse(users: SearchUsersQuery.Search): PaginatedResponse<User> {
            return PaginatedResponse(
                endCursor = users.pageInfo.endCursor,
                hasNextPage = users.pageInfo.hasNextPage,
                totalCount = users.userCount,
                nodes = User.fromSearchUsersNodeList(users.nodes)
            )
        }
    }
}