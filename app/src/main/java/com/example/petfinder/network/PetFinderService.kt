package com.example.petfinder.network

import com.example.petfinder.models.network.AnimalCallResponse
import com.example.petfinder.models.network.AnimalResponse
import com.example.petfinder.models.network.AnimalsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PetFinderService {
    @GET("animals")
    suspend fun getAnimals(
        @Query("size") size: String? = null,
        @Query("location") location: String? = null,
        @Query("page") pageNumber: Int? = null): AnimalsResponse

    @GET("animals/{id}")
    suspend fun getAnimal(@Path("id") animalId: Int): AnimalCallResponse
}