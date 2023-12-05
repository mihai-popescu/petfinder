package com.example.petfinder.models.network

import android.util.Log
import com.example.petfinder.exceptions.AuthorizationException
import com.example.petfinder.exceptions.NoConnectivityException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Log.e("NETWORK ERROR", throwable.toString())
            when (throwable) {
                NoConnectivityException -> ResultWrapper.NoConnectionError
                AuthorizationException -> ResultWrapper.AuthorizationNotFoundError
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.response()?.errorBody()?.byteStream()?.bufferedReader()?.use(
                        BufferedReader::readText) ?: ""
                    if (code == 400 && errorResponse == "Invalid API key")
                        return@withContext ResultWrapper.AuthorizationNotFoundError
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}