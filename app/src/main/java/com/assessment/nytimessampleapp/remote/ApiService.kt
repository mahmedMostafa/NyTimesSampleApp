package com.assessment.nytimessampleapp.remote

import com.assessment.nytimessampleapp.common.API_KEY
import com.assessment.nytimessampleapp.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("viewed/{number_of_days}.json")
    suspend fun getLatestNews(
        @Path("number_of_days") noOfDays: Int,
        @Query("api-key") apiKey: String = API_KEY
    ): NewsResponse
}