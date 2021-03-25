package com.assessment.nytimessampleapp.utils

enum class DaysCount {
    ONE_DAY,
    SEVEN_DAYS,
    THIRTY_DAYS
}

fun getNoOfDays(count: DaysCount): Int {
    return when (count) {
        DaysCount.ONE_DAY -> 1
        DaysCount.SEVEN_DAYS -> 7
        DaysCount.THIRTY_DAYS -> 30
    }
}