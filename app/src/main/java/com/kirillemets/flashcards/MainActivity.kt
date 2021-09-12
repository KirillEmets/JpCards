package com.kirillemets.flashcards

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kirillemets.flashcards.review.ReviewFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.danlew.android.joda.JodaTimeAndroid

lateinit var bottomNavigation: BottomNavigationView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: ReviewFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        JodaTimeAndroid.init(this)
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = preferenceManager.getString("theme", "Auto")
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                "Auto" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                "Light" -> AppCompatDelegate.MODE_NIGHT_NO
                "Black" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )

        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        val navController = this.findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.reviewStarterFragment,
            R.id.addWordFragment,
            R.id.myDictionaryFragment,
            R.id.settingsFragment
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigation.setOnItemSelectedListener { item ->
            if(bottomNavigation.selectedItemId == item.itemId)
                return@setOnItemSelectedListener false

            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            when(item.itemId) {
                R.id.page_review -> {
                    navController.navigate(R.id.action_global_reviewStarterFragment)
                    true
                }
                R.id.page_search -> {
                    navController.navigate(R.id.action_global_addWordFragment)
                    true
                }
                R.id.page_my_words -> {
                    navController.navigate(R.id.action_global_myDictionaryFragment)
                    true
                }
                R.id.page_settings -> {
                    navController.navigate(R.id.action_global_settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        onUpNavigation()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        onUpNavigation()
        super.onBackPressed()
    }

    private fun onUpNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        if(navController.currentDestination?.id == R.id.reviewFragment) {
            if(viewModel.reviewGoing) {
                viewModel.endReview()
            }
        }
    }
}

