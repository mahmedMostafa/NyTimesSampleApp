package com.assessment.nytimessampleapp.features.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.assessment.nytimessampleapp.repositories.MainHomeRepository
import com.assessment.nytimessampleapp.repositories.MainRepository
import com.assessment.nytimessampleapp.utils.DaysCount
import com.assessment.nytimessampleapp.utils.Resource
import com.assessment.nytimessampleapp.utils.getNoOfDays
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val repository: MainHomeRepository,
    private @Assisted val state: SavedStateHandle
) : ViewModel() {

    private var daysCountFlow =
        MutableStateFlow(state.get<DaysCount>("days_count") ?: DaysCount.ONE_DAY)
        set(value) {
            state["days_count"] = value
            field = value
        }

    val latestNewsFlow = combine(
        daysCountFlow
    ) { count -> count }.flatMapLatest {
        repository.getLatestNews(getNoOfDays(it.first()))
    }

    val latestNews = latestNewsFlow.asLiveData()

    fun getLatestNews(count: DaysCount) = viewModelScope.launch {
        daysCountFlow.emit(count)
    }

    fun refreshNews() = viewModelScope.launch {
        daysCountFlow.emit(daysCountFlow.value)
    }

//    fun getLatestNews() = viewModelScope.launch {
//        repository.getLatestNews().collect {
//            _latestNews.emit(it)
//        }
//    }

}
