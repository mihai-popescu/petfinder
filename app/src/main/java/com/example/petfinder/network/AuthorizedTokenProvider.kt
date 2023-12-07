package com.example.petfinder.network

import com.example.petfinder.exceptions.AuthorizationException
import com.example.petfinder.extensions.ifNotNull
import com.example.petfinder.managers.SharedPreferencesManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class AuthorizedTokenProvider: KoinComponent {
    companion object {
        private const val GRANT_TYPE = "client_credentials"
        private const val API_KEY = "8FvB92COL3loJkRHBozGPLOVKZTG4CgXal6Dou6EjsH5lj2SXB"
        private const val API_SECRET = "zcYSA3CrhG6yW1dc539o8rAVgj7ecwLUaYHTSe3s"
    }

    private val unauthorizedService: UnauthorizedService by inject()
    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    private var authorizedToken: String? = null
    private var tokenExpirationTime: Long? = null

    suspend fun getToken(): String {
        return ifNotNull(authorizedToken, tokenExpirationTime) { token, expirationTime ->
            if (expirationTime > Calendar.getInstance().timeInMillis) {
                token
            } else {
                invalidateToken()
                fetchToken()
            }
        } ?: kotlin.run {
            val (token, expirationTime) = sharedPreferencesManager.getToken()
            if (token != null && expirationTime > Calendar.getInstance().timeInMillis) {
                authorizedToken = token
                tokenExpirationTime = expirationTime
                token
            } else
            fetchToken()
        }
    }

    private fun invalidateToken() {
        authorizedToken = null
        tokenExpirationTime = null
    }

    private fun storeNewToken(token: String, validity: Int) {
        authorizedToken = token
        val expirationTime = Calendar.getInstance().timeInMillis + validity * 60 * 1000
        tokenExpirationTime = expirationTime
        sharedPreferencesManager.setToken(token, expirationTime)
    }

    private suspend fun fetchToken(): String {
        val (token, validity) = requestToken()
        storeNewToken(token, validity)
        return token
    }

    private suspend fun requestToken(): Pair<String, Int> {
        val result = unauthorizedService.getAccessToken(GRANT_TYPE, API_KEY, API_SECRET)
        return ifNotNull(result.accessToken, result.expiresIn) { token, expiresIn ->
            token to expiresIn.toInt()
        } ?: throw AuthorizationException
    }

    fun forceReauthorization(): Boolean {
        return authorizedToken?.let {
            authorizedToken = null
            tokenExpirationTime = null
            sharedPreferencesManager.setToken(null, null)
            true
        } ?: false
    }
}