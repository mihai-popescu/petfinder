package com.example.petfinder.repositories

import com.example.petfinder.managers.SearchCriteriaManager
import com.example.petfinder.models.Animal
import com.example.petfinder.models.Breeds
import com.example.petfinder.models.Photo
import com.example.petfinder.models.Size
import com.example.petfinder.models.network.AnimalResponse
import com.example.petfinder.models.network.AnimalsResponse
import com.example.petfinder.models.network.BreedsResponse
import com.example.petfinder.models.network.PhotoResponse
import com.example.petfinder.models.network.ResultWrapper
import com.example.petfinder.models.network.safeApiCall
import com.example.petfinder.network.PetFinderService
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetRepository: KoinComponent {
    private val service: PetFinderService by inject()
    private val searchCriteriaManager: SearchCriteriaManager by inject()

    suspend fun search(pageNumber: Int
    ): Result<List<Animal>> {
        val searchCriteria = searchCriteriaManager.searchCriteria
        return when (val response = safeApiCall(Dispatchers.IO) { service.getAnimals(
            size = if (searchCriteria.size.isEmpty()) null else searchCriteria.size.fold("") { acc: String, size: Size ->
                "$acc${size.value},"
            }.dropLast(1),
            location = searchCriteria.location,
            pageNumber = pageNumber
        ) }) {
            is ResultWrapper.Success -> {
                Result.Success(makeAnimals(response.value))
            }

            is ResultWrapper.AuthorizationNotFoundError -> Result.AuthorizationNotFoundError(
                response.exception
            )

            is ResultWrapper.NetworkError -> Result.NetworkError(response.exception)
            is ResultWrapper.NoConnectionError -> Result.NoConnectionError(response.exception)
            is ResultWrapper.GenericError -> Result.Error(Exception(response.error))
        }
    }
}



private fun makeAnimals(animalsResponse: AnimalsResponse): List<Animal> =
    animalsResponse.animals?.map { makeAnimal(it) } ?: emptyList()


fun makeAnimal(input: AnimalResponse): Animal = Animal(
    id = input.id ?: 0L,
    organizationId = input.organizationId ?: "",
    breeds = makeBreeds(input.breeds),
    gender = input.gender ?: "unknown",
    size = input.size ?: "medium",
    name = input.name ?: "",
    photos = makePhotos(input.photos),
    status = input.status ?: "adoptable",
    distance = input.distance ?: .0
)

private fun makeBreeds(input: BreedsResponse?): Breeds = Breeds(
    primary = input?.primary ?: "",
    secondary = input?.secondary ?: "",
    isMixed = input?.mixed ?: input?.secondary.isNullOrBlank(),
    isUnknown = input?.unknown ?: input?.primary.isNullOrBlank() && input?.secondary.isNullOrBlank()
)

private fun makePhotos(input: List<PhotoResponse>?): List<Photo> = input?.map { makePhoto(it) } ?: emptyList()

private fun makePhoto(input: PhotoResponse): Photo = Photo(input.small ?: "", input.medium ?: "", input.large ?: "", input.full ?: "")