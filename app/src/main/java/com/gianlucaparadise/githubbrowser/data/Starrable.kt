package com.gianlucaparadise.githubbrowser.data

import com.gianlucaparadise.githubbrowser.fragment.StarrableFragment

data class Starrable(
    val id: String,
    /**
     * A count of users who have starred this starrable.
     */
    val stargazersCount: Int,
    /**
     * Returns a boolean indicating whether the viewing user has starred this starrable.
     */
    val viewerHasStarred: Boolean
) {
    companion object {

        fun fromStarrableFragment(starrableFragment: StarrableFragment?): Starrable? {
            if (starrableFragment == null) return null

            return Starrable(
                id = starrableFragment.id,
                stargazersCount = starrableFragment.stargazers.totalCount,
                viewerHasStarred = starrableFragment.viewerHasStarred
            )
        }
    }
}