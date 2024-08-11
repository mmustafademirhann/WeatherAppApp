package com.example.vbchatgptweatherapp.domain.network

sealed class Response<T>(val data: T?, val message: String?,
                         val errorResponse: String?) {
    class Success<T>(data: T) : Response<T>(data, null,null)
    class Error<T>(message: String) : Response<T>(null, message,null)
    class ErrorResponse<T>(errorResponse: String) : Response<T>(null, null,errorResponse)
    class LoadingState<T>(data: T?=null): Response<T>(data, null,null)
}
