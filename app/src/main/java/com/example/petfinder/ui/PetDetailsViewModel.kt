package com.example.petfinder.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinder.models.Animal
import com.example.petfinder.models.network.ResultWrapper
import com.example.petfinder.models.network.safeApiCall
import com.example.petfinder.network.PetFinderService
import com.example.petfinder.repositories.makeAnimal
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetDetailsViewModel(private val originalAnimal: Animal): ViewModel(), KoinComponent {
    companion object {
        const val TAG = "PetDetailsViewModel"
    }
    private val service: PetFinderService by inject()
    private val _animal = MutableStateFlow(originalAnimal)
    val animal = _animal.asStateFlow()

    init {
        viewModelScope.launch {
            when(val response = safeApiCall(IO) {
                service.getAnimal(originalAnimal.id.toInt())
            }) {
                is ResultWrapper.Success -> _animal.value = composeAnimalData(originalAnimal, makeAnimal(response.value))
                else -> Log.e(TAG, "Fetch pet error")
            }
        }
    }

    fun composeAnimalData(originalAnimal: Animal, newData:Animal): Animal =
        Animal(
            id = originalAnimal.id,
            organizationId = originalAnimal.organizationId,
            breeds = if (newData.breeds.primary.isBlank()) originalAnimal.breeds else newData.breeds,
            gender = if (newData.gender != "unknown") newData.gender else originalAnimal.gender,
            size = if (newData.size != "medium") newData.size else originalAnimal.size,
            name = originalAnimal.name,
            photos = if (newData.photos.firstOrNull()?.full.isNullOrBlank()) originalAnimal.photos else newData.photos,
            status = newData.status.ifBlank { originalAnimal.status },
            distance = if (newData.distance > 0) newData.distance else originalAnimal.distance
            )
}