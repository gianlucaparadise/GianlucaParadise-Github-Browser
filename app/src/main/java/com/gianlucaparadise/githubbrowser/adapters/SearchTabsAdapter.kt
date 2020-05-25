package com.gianlucaparadise.githubbrowser.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.ui.searchresults.SearchResultsFragment

class SearchTabsAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    companion object {
        val tabs = arrayOf(TabType.PEOPLE, TabType.REPOSITORIES)
    }

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        return SearchResultsFragment.newInstance()
    }

    enum class TabType(@StringRes val title: Int) {
        PEOPLE(R.string.search_people_tab_title),
        REPOSITORIES(R.string.search_repositories_tab_title)
    }
}