package com.kirillemets.flashcards.review

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kirillemets.flashcards.MainActivity
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.model.FlashCard
import com.kirillemets.flashcards.model.FlashCardRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ReviewFragmentTest {

    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject lateinit var repository: FlashCardRepository

    @Before
    fun init() {
        rule.inject()
    }

    @Test
    fun recreate() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_start_review))
            .check(matches(isDisplayed()))
        scenario.recreate()
    }

    @Test
    fun noWordsReviewButtonIsNotClickable() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
    }

    @Test
    fun hasWordsClickOnReviewButton() {
        repository.insert(FlashCard(0, "Japanese", "reading", "English"))
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMissButton() {
        repository.insert(FlashCard(0, "Japanese", "reading", "English"))
        ActivityScenario.launch(MainActivity::class.java)
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
        repository.insert(FlashCard(0, "Japanese", "reading", "English"))
        ActivityScenario.launch(MainActivity::class.java)
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
        repository.insert(FlashCard(0, "Japanese", "reading", "English"))
        ActivityScenario.launch(MainActivity::class.java)
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

    @Test
    fun removeCardImmediately() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        repository.insert(FlashCard(0, "Jap2", "", "eng2"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.delete_card))
            .perform(click())

        repeat(2) {
            onView(withId(R.id.button_show_answer))
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.button_miss))
                .perform(click())
        }

        runBlocking { Assert.assertEquals(1, repository.getAllSuspend().size) }
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
    }

    @Test
    fun removeAllCards() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        repository.insert(FlashCard(0, "Jap2", "", "eng2"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        repeat(2) {
            Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText(R.string.delete_card))
                .perform(click())
        }

        runBlocking { Assert.assertEquals(0, repository.getAllSuspend().size) }
        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
    }

    @Test
    fun removeLastCard() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        repository.insert(FlashCard(0, "Jap2", "", "eng2"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        repeat(3) {
            onView(withId(R.id.button_show_answer))
                .check(matches(isDisplayed()))
                .perform(click())

            onView(withId(R.id.button_miss))
                .perform(click())
        }

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.delete_card))
            .perform(click())

        runBlocking { Assert.assertEquals(1, repository.getAllSuspend().size) }
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
    }

    @Test
    fun pressBackOnReview() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        Espresso.pressBack()

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
    }

    @Test
    fun startChangePageAndGoBack() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.button_easy))
            .perform(click())

        onView(allOf(withText("settings"), isDisplayed()))
            .perform(click())

        onView(allOf(withText("review"), isDisplayed()))
            .perform(click())

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.button_easy))
            .perform(click())

        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
    }

    @Test
    fun restartWhileReview() {
        repository.insert(FlashCard(0, "Jap", "", "eng"))
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.button_easy))
            .perform(click())

        scenario.recreate()

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
            .perform(click())

        scenario.recreate()

        onView(withId(R.id.button_easy))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
    }
}