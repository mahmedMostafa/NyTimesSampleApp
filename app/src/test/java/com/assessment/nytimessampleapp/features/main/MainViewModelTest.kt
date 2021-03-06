package com.assessment.nytimessampleapp.features.main

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.data.FakeMainRepository
import com.assessment.nytimessampleapp.data.PreferencesManager
import com.assessment.nytimessampleapp.data.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.utils.*
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepository: MainHomeRepository
    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun initViewModel() {
        preferencesManager = Mockito.mock(PreferencesManager::class.java)
        fakeRepository = FakeMainRepository()
        viewModel = MainViewModel(fakeRepository, preferencesManager)
    }

    @Test
    fun `at first the state should be loading and list contains right`() = runBlocking {
        //when
        val dayCountFlow = flow {
            emit(DaysCount.ONE_DAY)
        }
        whenever(preferencesManager.preferencesFlow).thenReturn(dayCountFlow)
        val flow =
            fakeRepository.getLatestNews(getNoOfDays(preferencesManager.preferencesFlow.first()))

        //Then
        assertThat(flow.first() is Resource.Loading, `is`(true))
        flow.collect {
            it.onSuccess { news ->
                assertThat(news, Matchers.contains(NewsModel(title = "Title 1")))
            }
        }
    }

    @Test
    fun `should return error is verified`() = runBlocking {
        //Given
        (fakeRepository as FakeMainRepository).setShouldReturnError(true)

        //When
        val flow = fakeRepository.getLatestNews(1)
        assertTrue(flow.first() is Resource.Error)
    }
}