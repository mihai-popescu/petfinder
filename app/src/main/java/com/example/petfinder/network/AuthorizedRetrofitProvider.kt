package com.example.petfinder.network

import com.example.petfinder.BuildConfig
import com.example.petfinder.exceptions.AuthorizationException
import com.example.petfinder.exceptions.NoConnectivityException
import com.example.petfinder.managers.NetworkManager
import com.example.petfinder.managers.NetworkState
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.inject

class AuthorizedRetrofitProvider: BaseRetrofitProvider() {
    companion object {
        const val HEADER_KEY_AUTHORIZATION = "Authorization"
    }
    private val tokenProvider: AuthorizedTokenProvider by inject()

    override fun configureHttpClient(builder: OkHttpClient.Builder) {
        builder.addInterceptor(
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.BASIC
            )
        )
        builder.addNetworkInterceptor(buildDefaultHeaderInterceptor())
        builder.addInterceptor(buildNetworkInterceptor())
    }

    private fun buildDefaultHeaderInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            try {
                val original = chain.request()
                val request: Request = buildDefaultHeaders(original).build()
                chain.proceed(request)
            } catch (ex: AuthorizationException) {
                throw ex
            }
        }
    }

    private fun buildNetworkInterceptor(): Interceptor {
        return object : Interceptor {
            private val networkManager: NetworkManager by inject()
            override fun intercept(chain: Interceptor.Chain): Response {
                return if (networkManager.networkState.value == NetworkState.CONNECTED) {
                    chain.proceed(chain.request())
                } else {
                    throw NoConnectivityException
                }
            }
        }
    }

    private fun buildDefaultHeaders(original: Request): Request.Builder {
        val token = runBlocking {
            tokenProvider.getToken()
        }
        return original.newBuilder().apply {
           header(HEADER_KEY_AUTHORIZATION, "Bearer $token")
       }
   }
}