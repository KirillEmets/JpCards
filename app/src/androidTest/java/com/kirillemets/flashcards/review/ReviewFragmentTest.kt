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
import com.kirillemets.flashcards.MockDaoTest
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.FlashCard
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ReviewFragmentTest: MockDaoTest() {
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
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.button_show_answer))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMissButton() {
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
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
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
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
        dao.insert(FlashCard(0, "Japanese", "reading", "English"))
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
        dao.insert(FlashCard(0, "Jap", "", "eng"))
        dao.insert(FlashCard(0, "Jap2", "", "eng2"))
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

        Assert.assertEquals(1, dao.getAllBlocking().size)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
    }

    @Test
    fun removeAllCards() {
        dao.insert(FlashCard(0, "Jap", "", "eng"))
        dao.insert(FlashCard(0, "Jap2", "", "eng2"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
            .perform(click())

        repeat(2) {
            Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText(R.string.delete_card))
                .perform(click())
        }

        Assert.assertEquals(0, dao.getAllBlocking().size)
        onView(withId(R.id.button_start_review))
            .check(matches(not(isClickable())))
    }

    @Test
    fun removeLastCard() {
        dao.insert(FlashCard(0, "Jap", "", "eng"))
        dao.insert(FlashCard(0, "Jap2", "", "eng2"))
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

        Assert.assertEquals(1, dao.getAllBlocking().size)
        onView(withId(R.id.button_start_review))
            .check(matches(isClickable()))
    }

    @Test
    fun pressBackOnReview() {
        dao.insert(FlashCard(0, "Jap", "", "eng"))
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
        dao.insert(FlashCard(0, "Jap", "", "eng"))
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
        dao.insert(FlashCard(0, "Jap", "", "eng"))
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