package com.gianlucaparadise.githubbrowser.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.ui.search.SearchRepositoryResultsFragment
import com.gianlucaparadise.githubbrowser.ui.search.SearchUserResultsFragment

class SearchTabsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    companion object {
        val tabs = arrayOf(TabType.PEOPLE, TabType.REPOSITORIES)
    }

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        val currentTab = tabs[position]
        return when(currentTab) {
            TabType.PEOPLE -> SearchUserResultsFragment.newInstance()
            TabType.REPOSITORIES -> SearchRepositoryResultsFragment.newInstance()
        }
    }

    enum class TabType(@StringRes val title: Int) {
        PEOPLE(R.string.search_people_tab_title),
        REPOSITORIES(R.string.search_repositories_tab_title)
    }
}