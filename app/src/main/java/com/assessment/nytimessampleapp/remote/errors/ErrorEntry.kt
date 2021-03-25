package com.assessment.nytimessampleapp.remote.errors

sealed class ErrorEntry {

    object Network : ErrorEntry()

    object NotFound : ErrorEntry()

    object AccessDenied : ErrorEntry()

    object Unauthorized : ErrorEntry()

    //server is up but might be overloaded with requests
    object ServiceUnavailable : ErrorEntry()

    //server is down or being upgraded
    object ServerDown : ErrorEntry()

    object ServerError : ErrorEntry()

    object BadRequest : ErrorEntry()

    object Unknown : ErrorEntry()

    object TimeOut : ErrorEntry()
}