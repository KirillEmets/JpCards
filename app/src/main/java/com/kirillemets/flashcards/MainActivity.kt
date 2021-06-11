package com.kirillemets.flashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.danlew.android.joda.JodaTimeAndroid


lateinit var bottomNavigation: BottomNavigationView

class MainActivity : AppCompatActivity() {
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
        })

        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        val navController = this.findNavController(R.id.nav_host_fragment)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if(bottomNavigation.selectedItemId == item.itemId)
                return@setOnNavigationItemSelectedListener false

            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            when(item.itemId) {
                R.id.page_1 -> {
                    navController.navigate(R.id.action_global_reviewFragment)
                    true
                }
                R.id.page_2 -> {
                    navController.navigate(R.id.action_global_addWordFragment)
                    true
                }
                R.id.page_3 -> {
                    navController.navigate(R.id.action_global_myDictionaryFragment)
                    true
                }
                R.id.page_4 -> {
                    navController.navigate(R.id.action_global_settingsFragment)
                    true
                }
                else -> false
            }
        }



    }
}

