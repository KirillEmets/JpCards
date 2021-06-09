package com.kirillemets.flashcards.review

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ReviewFragmentTest {
    private lateinit var dao: CardDatabaseDao

    @Before
    fun mockDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
        CardDatabase.setInstance(db)
        dao = db.flashCardsDao()
    }

    @Test
    fun recreate() {
        val scenario = launchFragmentInContainer<ReviewFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.button_start_review))
            .check(matches(isDisplayed()))
        scenario.recreate()
    }

    @Test
    fun noWordsReviewButtonIsNotClickable() {
        launchFragmentInContainer<ReviewFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
    }

    @Test
    fun hasWordsClickOnReviewButton() {
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
        launchFragmentInContainer<ReviewFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMissButton() {
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
        launchFragmentInContainer<ReviewFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        repeat(2) {
            onView(withId(R.id.button_show_answer))
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.button_miss))
                .perform(click())
        }

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnHardButton() {
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
        launchFragmentInContainer<ReviewFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        repeat(2) {
            onView(withId(R.id.button_show_answer))
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.button_hard))
                .perform(click())
        }

        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnEasyButton() {
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
        launchFragmentInContainer<ReviewFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        repeat(2) {
            onView(withId(R.id.button_show_answer))
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.button_easy))
                .perform(click())
        }

        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
            .check(matches(isDisplayed()))
    }
}