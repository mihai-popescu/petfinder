package com.example.petfinder.models.network

import com.google.gson.annotations.SerializedName

data class AnimalsResponse(
    val animals: List<AnimalResponse>?,
)

data class AnimalCallResponse(
    val animal: AnimalResponse?
)

data class AnimalResponse(
    val id: Long?,
    @SerializedName("organization_id")
    val organizationId: String?,
    val url: String?,
    val type: String?,
    val species: String?,
    val breeds: BreedsResponse?,
    val colors: ColorsResponse?,
    val age: String?,
    val gender: String?,
    val size: String?,
    val coat: String?,
    val name: String?,
    val description: String?,
    val photos: List<PhotoResponse>?,
    val videos: List<VideoResponse>?,
    val status: String?,
    val attributes: AttributesResponse?,
    val environment: EnvironmentResponse?,
    val tags: List< String>?,
    val contact: ContactResponse?,
    @SerializedName("published_at")
    val publishedAt: String?,
    val distance: Double?,
    @SerializedName("_links")
    val links: LinksResponse?
)

data class BreedsResponse(
    val primary: String?,
    val secondary: String?,
    val mixed: Boolean?,
    val unknown: Boolean?
)

data class ColorsResponse(
    val primary: String?,
    val secondary: String?,
    val tertiary: String?
)


data class PhotoResponse(
    val small: String?,
    val medium: String?,
    val large: String?,
    val full: String?
)

data class VideoResponse(
    val embed: String?
)

data class AttributesResponse(
    @SerializedName("spayed_neutered")
    val spayedNeutered: Boolean?,
    @SerializedName("house_trained")
    val houseTrained: Boolean?,
    val declawed: Boolean?,
    @SerializedName("special_needs")
    val specialNeeds: Boolean?,
    @SerializedName("shots_current")
    val shotsCurrent: Boolean?
)

data class EnvironmentResponse(
    val children: Boolean?,
    val dogs: Boolean?,
    val cats: Boolean?
)

data class ContactResponse(
    val email: String?,
    val phone: String?,
    val address: AddressResponse?
)

data class AddressResponse(
    val address1: String?,
    val address2: String?,
    val city: String?,
    val state: String?,
    val postcode: String?,
    val country: String?
)

data class LinksResponse(
    val self: LinkResponse?,
    val type: LinkResponse?,
    val organization: LinkResponse?
)

data class LinkResponse(
    val href: String?
)