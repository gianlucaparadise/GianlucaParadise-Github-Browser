package com.gianlucaparadise.githubbrowser.ui.base

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gianlucaparadise.githubbrowser.R
import com.gianlucaparadise.githubbrowser.network.LoginHelper
import com.gianlucaparadise.githubbrowser.ui.loginbenefits.LoginBenefitsFragment

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
        LoginHelper.logout()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        findNavController().navigate(R.id.loginBenefitsFragment, null, navOptions)

        return true
    }
}