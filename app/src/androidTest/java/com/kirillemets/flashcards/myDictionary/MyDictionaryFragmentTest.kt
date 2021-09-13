package com.kirillemets.flashcards.myDictionary

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.kirillemets.flashcards.MainActivity
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.model.FlashCard
import com.kirillemets.flashcards.model.FlashCardRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MyDictionaryFragmentTest{

    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject lateinit var repository: FlashCardRepository

    @Before
    fun init() {
        rule.inject()
    }

    @Test
    fun removeCardAndClear() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        repository.insert(FlashCard(0, "Jap2", "", "eng2"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.page_my_words))
            .perform(click())

        onView(withText("Jap2"))
            .check(matches(isDisplayed()))
            .perform(longClick())

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.item_remove_string))
            .perform(click())

        runBlocking { Assert.assertEquals(1, repository.getAllSuspend().size) }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.item_reset_string))
            .perform(click())
    }
}
