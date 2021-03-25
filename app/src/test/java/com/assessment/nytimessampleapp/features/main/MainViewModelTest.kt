package com.assessment.nytimessampleapp.features.main

import com.assessment.nytimessampleapp.utils.TestCoroutineRule
import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.data.FakeMainRepository
import com.assessment.nytimessampleapp.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.utils.Resource
import com.assessment.nytimessampleapp.utils.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepository: MainHomeRepository

    @Before
    fun initViewModel() {
        fakeRepository =
            FakeMainRepository()
        viewModel = MainViewModel(fakeRepository)
    }

    @Test
    fun `at first the state should be loading and list contains right`() = runBlocking {
        //when
        val flow = fakeRepository.getLatestNews(1)

        //Then
        assertThat(flow.first() is Resource.Loading, `is`(true))
        flow.collect {
            it.onSuccess { news ->
                assertThat(news, Matchers.contains(NewsModel(title = "Title 1")))
            }
        }
    }
}