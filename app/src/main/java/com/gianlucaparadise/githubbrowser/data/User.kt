package com.gianlucaparadise.githubbrowser.data

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
    }
}