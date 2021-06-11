package com.kirillemets.flashcards.myDictionary

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
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
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MyDictionaryFragmentTest {
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
        val scenario = launchFragmentInContainer<MyDictionaryFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.card))
            .check(matches(not(isDisplayed())))
        scenario.recreate()
    }

    @Test
    fun removeCardAndClear() {
        dao.insert(FlashCard(0, "Jap", "", "eng"))
        dao.insert(FlashCard(0, "Jap2", "", "eng2"))
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.page_my_words))
            .perform(click())

        onView(withText("Jap2"))
            .check(matches(isDisplayed()))
            .perform(longClick())

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.item_remove_string))
            .perform(click())

        Assert.assertEquals(1, dao.getAllBlocking().size)

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.item_reset_string))
            .perform(click())
    }
}