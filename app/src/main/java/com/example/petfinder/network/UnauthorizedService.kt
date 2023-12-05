package com.example.petfinder.network

import com.example.petfinder.models.network.AuthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UnauthorizedService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getAccessToken(@Field("grant_type") grantType: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String): AuthResponse
}