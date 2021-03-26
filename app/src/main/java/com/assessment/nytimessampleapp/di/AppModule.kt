package com.assessment.nytimessampleapp.di

import com.assessment.nytimessampleapp.common.BASE_URL
import com.assessment.nytimessampleapp.remote.ApiService
import com.assessment.nytimessampleapp.data.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.data.repositories.MainRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideMoshiBuilder(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideMainRepository(apiService: ApiService): MainHomeRepository {
        return MainRepository(apiService) as MainHomeRepository
    }
}