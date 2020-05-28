package com.gianlucaparadise.githubbrowser.ui.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.gianlucaparadise.githubbrowser.R

/**
 * This is the base fragment for the main part of the app (e.g. Home, Search)
 */
abstract class BaseMainFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.toolbar_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> logout()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout(): Boolean {
        return true
    }
}