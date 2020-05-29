package com.gianlucaparadise.githubbrowser

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gianlucaparadise.githubbrowser.network.LoginHelper
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupNav()
    }

    private fun setupNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navController.addOnDestinationChangedListener(onDestinationChanged)

        // Depending on login state, I choose the start fragment
        if (LoginHelper.isLoggedIn) {
            navGraph.startDestination = R.id.homeFragment
        } else {
            navGraph.startDestination = R.id.loginBenefitsFragment
        }
        navController.graph = navGraph

        // Setting Up TabBar with Navigation Controller
        tabBar.setupWithNavController(navController)

        // Setting Up ActionBar with Navigation Controller
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.loginBenefitsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val onDestinationChanged =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginBenefitsFragment, R.id.loginWebViewFragment -> {
                    tabBar.isVisible = false // TODO: animate nicely
                }
                else -> {
                    tabBar.isVisible = true // TODO: animate nicely
                }
            }
        }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // This is to hide the keyboard on touch outside
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
