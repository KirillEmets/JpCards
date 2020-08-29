package com.kirillemets.flashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kirillemets.flashcards.network.JishoApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

lateinit var bottomNavigation: BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                    Log.i("HELLO", "1")
                    true
                }
                R.id.page_2 -> {
                    Log.i("HELLO", "2")
                    true
                }
                R.id.page_3 -> {
                    Log.i("HELLO", "3")
                    true
                }
                else -> false
            }
        }



    }
}

