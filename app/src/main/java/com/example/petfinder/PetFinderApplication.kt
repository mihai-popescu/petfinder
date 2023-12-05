package com.example.petfinder

import android.app.Application
import com.example.petfinder.managers.NetworkManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PetFinderApplication : Application(),KoinComponent {
    private val networkManager: NetworkManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PetFinderApplication)
            modules(PetFinderModule)
        }
        networkManager.registerCallback()
    }
}