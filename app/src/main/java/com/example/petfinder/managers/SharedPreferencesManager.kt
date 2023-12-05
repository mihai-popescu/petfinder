package com.example.petfinder.managers

import android.content.Context

interface SharedPreferencesManager {
    fun getToken(): Pair<String?, Long>
    fun setToken(token: String, expiration: Long)
}

class SharedPreferencesManagerImpl(context: Context) : SharedPreferencesManager {
    private val sharedPrefs = context.getSharedPreferences(PET_FINDER_PREFS, Context.MODE_PRIVATE)

    companion object {
        private const val PET_FINDER_PREFS = "petfinderprefs"
        private const val TOKEN = "token"
        private const val EXPIRATION = "expiartion"
    }

    override fun getToken(): Pair<String?, Long> {
        return with(sharedPrefs) {
            getString(TOKEN, null) to getLong(EXPIRATION, 0L)
        }
    }

    override fun setToken(token: String, expiration: Long) {
        with(sharedPrefs) {
            edit()?.let {
                it.putString(TOKEN, token)
                it.putLong(EXPIRATION, expiration)
            }
        }
    }
}