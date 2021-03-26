package com.assessment.nytimessampleapp.features.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.assessment.nytimessampleapp.data.PreferencesManager
import com.assessment.nytimessampleapp.data.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.utils.DaysCount
import com.assessment.nytimessampleapp.utils.getNoOfDays
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val repository: MainHomeRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val preferencesFlow = preferencesManager.preferencesFlow

    val latestNewsFlow = combine(
        preferencesFlow
    ) { filterPreferences -> filterPreferences }.flatMapLatest { countPreferences ->
        repository.getLatestNews(getNoOfDays(countPreferences.first()))
    }

    val latestNews = latestNewsFlow.asLiveData()

    fun getLatestNews(count: DaysCount) = viewModelScope.launch {
        preferencesManager.setDayCount(count)
    }

    fun refreshNews() = viewModelScope.launch {
        preferencesManager.setDayCount(preferencesFlow.first())
    }
}
