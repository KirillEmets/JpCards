package com.kirillemets.flashcards.ui

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.domain.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    @Inject lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            when (appPreferences.theme) {
                AppPreferences.Theme.Auto -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                AppPreferences.Theme.Light -> AppCompatDelegate.MODE_NIGHT_NO
                AppPreferences.Theme.Black -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )

        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val navController = this.findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,
            R.id.addWordFragment,
            R.id.myDictionaryFragment,
            R.id.settingsFragment
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)

        bottomNavigation.setOnItemSelectedListener { item ->
            if (bottomNavigation.selectedItemId == item.itemId)
                return@setOnItemSelectedListener false

            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            when (item.itemId) {
                R.id.page_review -> {
                    navController.navigate(R.id.action_global_reviewStarterFragment)
                    true
                }
                R.id.addWordFragment -> {
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
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}