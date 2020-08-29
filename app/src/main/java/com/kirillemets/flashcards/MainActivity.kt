package com.kirillemets.flashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


lateinit var bottomNavigation: BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        val navController = this.findNavController(R.id.nav_host_fragment)


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if(bottomNavigation.selectedItemId == item.itemId)
                return@setOnNavigationItemSelectedListener false
            when(item.itemId) {
                R.id.page_1 -> {
                    Log.i("HELLO", "1")
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
                else -> false
            }
        }



    }
}

