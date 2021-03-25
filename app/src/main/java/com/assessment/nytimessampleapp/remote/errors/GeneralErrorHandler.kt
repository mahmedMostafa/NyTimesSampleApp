package com.assessment.nytimessampleapp.remote.errors

import com.assessment.nytimessampleapp.R
import com.assessment.nytimessampleapp.utils.Resource
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class GeneralErrorHandler {
    fun getError(throwable: Throwable): ErrorEntry {
        return when (throwable) {
            is IOException -> ErrorEntry.Network

            is TimeoutException -> ErrorEntry.TimeOut

            is HttpException -> when (throwable.code()) {

                HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntry.NotFound

                HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntry.AccessDenied

                HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorEntry.Unauthorized

                HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntry.ServiceUnavailable

                HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntry.ServerError

                HttpURLConnection.HTTP_GATEWAY_TIMEOUT, HttpURLConnection.HTTP_CLIENT_TIMEOUT -> ErrorEntry.TimeOut

                HttpURLConnection.HTTP_BAD_GATEWAY -> ErrorEntry.ServerDown

                HttpURLConnection.HTTP_BAD_REQUEST -> ErrorEntry.BadRequest

                else -> ErrorEntry.Unknown
            }

            is UnknownHostException -> ErrorEntry.Unknown

            else -> ErrorEntry.Unknown
        }
    }
}

fun handleErrors(e: Throwable): Resource.Error {
    return when (GeneralErrorHandler().getError(e)) {
        is ErrorEntry.Network -> {
            Resource.Error(R.string.error_network)
        }
        is ErrorEntry.ServiceUnavailable -> {
            //show service error then go back
            Resource.Error(R.string.error_service_unavailable)
        }
        is ErrorEntry.ServerDown -> {
            //show service error
            Resource.Error(R.string.error_server_down)
        }
        is ErrorEntry.TimeOut -> {
            //show service error then reload the request
            Resource.Error(R.string.error_timeout)
        }
        is ErrorEntry.Unauthorized -> {
            //show service error then go to login
            Resource.Error(R.string.error_unauthorized)
        }
        is ErrorEntry.NotFound -> {
            //show general error message
            Resource.Error(R.string.error_unknown)
        }
        is ErrorEntry.ServerError -> {
            //show service error then reload the request
            Resource.Error(R.string.error_server_error)
        }
        is ErrorEntry.Unknown -> {
            Resource.Error(R.string.error_unknown)
        }
        else -> {
            Resource.Error(R.string.error_unknown)
        }
    }
}
