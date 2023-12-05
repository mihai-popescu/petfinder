package com.example.petfinder.models.network

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token_type")
    val tokenType:String?,
    @SerializedName("expires_in")
    val expiresIn: Long?,
    @SerializedName("access_token")
    val accessToken: String?)
