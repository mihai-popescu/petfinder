package com.example.petfinder.network

import com.example.petfinder.BuildConfig
import com.example.petfinder.exceptions.NoConnectivityException
import com.example.petfinder.managers.NetworkManager
import com.example.petfinder.managers.NetworkState
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseRetrofitProvider: KoinComponent {
    companion object {
        protected const val BASE_URL = "https://api.petfinder.com/v2/"
    }

    open fun getRetrofit(): Retrofit {
        return Retrofit.Builder().apply {
            configureRetrofit(this)
            baseUrl(BASE_URL)
            client(buildHttpClient())
        }.build()
    }

    private fun configureRetrofit(builder: Retrofit.Builder) {
        builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            configureHttpClient(this)
            build()
        }
    }

    open fun configureHttpClient(builder: OkHttpClient.Builder) {
        builder.addInterceptor(
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.BASIC
            )
        )
        builder.addInterceptor(buildNetworkInterceptor())
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

    fun get(): Retrofit {
        return getRetrofit()
    }
}