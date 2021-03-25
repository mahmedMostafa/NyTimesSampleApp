package com.assessment.nytimessampleapp.repositories

import com.assessment.nytimessampleapp.models.NewsModel
import com.assessment.nytimessampleapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MainHomeRepository {

    suspend fun getLatestNews(numOfDays: Int): Flow<Resource<List<NewsModel>>>
}