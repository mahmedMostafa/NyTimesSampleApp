package com.assessment.nytimessampleapp.data

import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.data.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.data.repositories.MainRepository
import com.assessment.nytimessampleapp.utils.TestCoroutineRule
import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.models.NewsResponse
import com.assessment.nytimessampleapp.remote.ApiService
import com.assessment.nytimessampleapp.utils.Resource
import com.assessment.nytimessampleapp.utils.onError
import com.assessment.nytimessampleapp.utils.onSuccess
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.IOException

@ExperimentalCoroutinesApi
class MainRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var repository: MainHomeRepository

    @Before
    fun init() {
        apiService = mock<ApiService>(ApiService::class.java)
        repository =
            MainRepository(
                apiService
            )
    }

    @Test
    fun `mocking service first collect is loading`() = runBlocking {
        //Given Mock Api Service
        val newsResponse = NewsResponse(
            numResults = 20,
            newsModels = listOf(NewsModel(title = "Title 1")),
            status = "NOT OK"
        )
        apiService.stub {
            onBlocking { getLatestNews(7) } doReturn newsResponse
        }
        //When
        val flow = repository.getLatestNews(7)

        //Then
        assertThat(flow.first() is Resource.Loading, `is`(true))
    }

    @Test
    fun `mocking service flow emits successfully`() = runBlocking {
        //Given Mock Api Service
        val newsResponse = NewsResponse(
            numResults = 20,
            newsModels = listOf(NewsModel(title = "Title 1")),
            status = "OK"
        )
        apiService.stub {
            onBlocking { getLatestNews(7) } doReturn newsResponse
        }

        //When
        val flow = repository.getLatestNews(7)

        //Then
        flow.collect { result ->
            result.onSuccess {
                assertThat(it, `is`(newsResponse.newsModels))
            }
        }
    }

    @Test
    fun `mocking service not ok flow emits error something went wrong `() = runBlocking {
        //Given Mock Api Service
        val newsResponse = NewsResponse(
            numResults = 20,
            newsModels = listOf(NewsModel(title = "Title 1")),
            status = "NOT OK"
        )
        apiService.stub {
            onBlocking { getLatestNews(7) } doReturn newsResponse
        }
        //When
        val flow = repository.getLatestNews(7)

        //Then
        flow.collect { result ->
            result.onError {
                assertThat(it, `is`(R.string.error_something_wrong))
            }
        }
    }

    @Test
    fun `mocking service throws exception flow emits no internet error`() = runBlocking {
        //Given Mock api service
        apiService.stub {
            onBlocking { getLatestNews(7) } doAnswer { throw IOException() }
        }
        //When
        val flow = repository.getLatestNews(7)

        //Then
        flow.collect { result ->
            result.onError {
                assertThat(it, `is`(R.string.error_network))
            }
        }
    }

}