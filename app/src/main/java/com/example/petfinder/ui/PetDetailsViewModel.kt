package com.example.petfinder.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinder.exceptions.NoConnectivityException
import com.example.petfinder.extensions.ifNotNull
import com.example.petfinder.models.Animal
import com.example.petfinder.models.network.ResultWrapper
import com.example.petfinder.models.network.safeApiCall
import com.example.petfinder.network.AuthorizedTokenProvider
import com.example.petfinder.network.PetFinderService
import com.example.petfinder.repositories.makeAnimal
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetDetailsViewModel(private val originalAnimal: Animal) : ViewModel(), KoinComponent {
    companion object {
        const val TAG = "PetDetailsViewModel"
    }

    private val service: PetFinderService by inject()
    private val tokenProvider: AuthorizedTokenProvider by inject()

    private val _animal = MutableStateFlow(originalAnimal)
    val animal = _animal.asStateFlow()

    private val _error: MutableStateFlow<Exception?> = MutableStateFlow(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch {
            when (val response = safeApiCall(IO) {
                service.getAnimal(originalAnimal.id.toInt())
            }) {
                is ResultWrapper.Success -> response.value.animal?.let {
                    _animal.value = makeAnimal(it)
                }

                is ResultWrapper.AuthorizationNotFoundError -> {
                    if (tokenProvider.forceReauthorization()) {
                        when (val response = safeApiCall(IO) {
                            service.getAnimal(originalAnimal.id.toInt())
                        }) {
                            is ResultWrapper.Success -> response.value.animal?.let {
                                _animal.value = makeAnimal(it)
                            }

                            is ResultWrapper.AuthorizationNotFoundError -> {
                                _error.value = response.exception
                                Log.e(TAG, "Authorization not found: ${response.exception}")
                            }

                            is ResultWrapper.NoConnectionError -> {
                                Log.e(TAG, "No network access")
                                _error.value = NoConnectivityException
                            }
                            else -> {
                                Log.e(TAG, "Unknown error")
                                _error.value = Exception("Unknown")
                            }
                        }
                    } else {
                        _error.value = response.exception
                        Log.e(TAG, "Authorization not found: ${response.exception}")
                    }
                }

                is ResultWrapper.NoConnectionError -> {
                    Log.e(TAG, "No network access")
                    _error.value = NoConnectivityException
                }
                else -> {
                    Log.e(TAG, "Unknown error")
                    _error.value = Exception("Unknown")
                }
            }
        }
    }
}