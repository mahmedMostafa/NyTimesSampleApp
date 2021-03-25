package com.assessment.nytimessampleapp.repositories

import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.remote.ApiService
import com.assessment.nytimessampleapp.remote.errors.handleErrors
import com.assessment.nytimessampleapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) : MainHomeRepository {

    override suspend fun getLatestNews(numOfDays: Int): Flow<Resource<List<NewsModel>>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getLatestNews(numOfDays)
            if (result.status == "OK") {
                emit(Resource.Success(result.newsModels ?: emptyList()))
            } else {
                emit(Resource.Error(R.string.error_something_wrong))
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(handleErrors(e))
        }
    }.flowOn(Dispatchers.IO)

}