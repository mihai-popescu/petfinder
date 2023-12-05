package com.example.petfinder.models.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null): ResultWrapper<Nothing>()
    data object NetworkError: ResultWrapper<Nothing>() {
        val exception = Exception("Network Error")
    }
    data object NoConnectionError: ResultWrapper<Nothing>() {
        val exception = Exception("No Connection")
    }
    data object AuthorizationNotFoundError: ResultWrapper<Nothing>() {
        val exception = Exception("Authorization not found")
    }

    fun getSuccessValue(): T? = (this as? Success)?.value
}