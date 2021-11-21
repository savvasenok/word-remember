package xyz.savvamirzoyan.wordremember.presentation.view.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import xyz.savvamirzoyan.wordremember.R

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

class MainActivity : AppCompatActivity() {

    private val navigationController by lazy {
        findNavController(R.id.main_navigation_host_fragment)
    }
    private val appBarConfig by lazy {
        AppBarConfiguration(
            navigationController.graph
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NavigationUI.setupActionBarWithNavController(this, navigationController)
        NavigationUI.setupWithNavController(
            findViewById<NavigationView>(R.id.navigation_view),
            navigationController
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navigationController, appBarConfig)
    }
}