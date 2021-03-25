package com.assessment.nytimessampleapp.features.detail

import android.content.Intent
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.features.main.MainActivity
import com.assessment.nytimessampleapp.models.NewsModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class DetailActivityTest {

    @Test
    fun testPassedTitleAndBody_areDisplayed() {

        val newsModel = NewsModel(title = "Title", abstract = "Body")
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            DetailActivity::class.java
        )
        intent.putExtra("news", newsModel)

        ActivityScenario.launch<DetailActivity>(intent)

        onView(withId(R.id.news_title_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.news_body_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testPassedTitleAndBody_haveTheRightTexts() {

        val newsModel = NewsModel(title = "Title", abstract = "Body")
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            DetailActivity::class.java
        )
        intent.putExtra("news", newsModel)

        ActivityScenario.launch<DetailActivity>(intent)

        onView(withId(R.id.news_title_text_view)).check(matches(withText("Title")))
        onView(withId(R.id.news_body_text_view)).check(matches(withText("Body")))
    }
}