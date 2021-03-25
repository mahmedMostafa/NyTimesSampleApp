package com.assessment.nytimessampleapp.data

import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMainRepository :
    MainHomeRepository {

    private var shoudReturnError: Boolean = false
    private var news = mutableListOf(
        NewsModel(title = "Title 1")
    )

    override suspend fun getLatestNews(numOfDays: Int): Flow<Resource<List<NewsModel>>> = flow {
        emit(Resource.Loading)
        if (!shoudReturnError) {
            emit(Resource.Success(news))
        } else {
            emit(Resource.Error(R.string.error_something_wrong))
        }
    }

    fun setShouldReturnError(error: Boolean) {
        shoudReturnError = error
    }
}