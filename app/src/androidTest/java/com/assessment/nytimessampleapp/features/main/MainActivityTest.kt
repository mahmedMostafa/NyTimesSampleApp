package com.assessment.nytimessampleapp.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.data.UiFakeMainRepository
import com.assessment.nytimessampleapp.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.utils.DaysCount
import com.assessment.nytimessampleapp.utils.MainCoroutineRule
import com.assessment.nytimessampleapp.utils.onLoading
import com.assessment.nytimessampleapp.utils.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepository: MainHomeRepository

    @Before
    fun init() {
        fakeRepository = UiFakeMainRepository()
        viewModel = MainViewModel(fakeRepository)
    }

    @Test
    fun recyclerviewDisplayedAndLoadingHidden_onSuccess() {

        //Given
        ActivityScenario.launch(MainActivity::class.java)

        //When
        viewModel.getLatestNews(DaysCount.SEVEN_DAYS)

        //Then
        testScope.launch {
            viewModel.latestNewsFlow.collect {
                it.onSuccess { news ->
                    onView(withId(R.id.swipe_refresh_layout)).check(matches(not(isDisplayed())))
                    onView(withId(R.id.news_recycler_view))
                        .check(matches(hasDescendant(withText("Title 1"))))
                }.onLoading {
                    onView(withId(R.id.swipe_refresh_layout)).check(matches(isDisplayed()))
                }
            }
        }
    }

    @Test
    fun onRecyclerItemClick_navigatesToDetailsActivity() {
        testScope.launch {
            //Given
            ActivityScenario.launch(MainActivity::class.java)

            //When
            viewModel.getLatestNews(DaysCount.SEVEN_DAYS)


            viewModel.latestNewsFlow.collect {
                it.onSuccess { news ->

                    //Then
                    onView(withId(R.id.news_recycler_view))
                        .perform(
                            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                                hasDescendant(withText("Title 1")), ViewActions.click()
                            )
                        )
                    onView(withId(R.id.news_title_text_view)).check(matches(isDisplayed()))
                    onView(withId(R.id.news_title_text_view)).check(matches(equalTo("Title 2")))
                }.onLoading {
                    onView(withId(R.id.swipe_refresh_layout)).check(matches(isDisplayed()))
                }
            }
        }

    }
}