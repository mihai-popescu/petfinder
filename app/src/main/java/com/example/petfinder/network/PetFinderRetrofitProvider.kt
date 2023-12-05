package com.example.petfinder.network

import retrofit2.Retrofit

object PetFinderRetrofitProvider {
    fun unauthorized(): Retrofit {
        return BaseRetrofitProvider().get()
    }

    fun authorized(): Retrofit {
        return AuthorizedRetrofitProvider().get()
    }
}