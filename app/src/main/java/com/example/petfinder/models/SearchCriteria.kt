package com.example.petfinder.models

data class SearchCriteria(
    val type: String? = null,
    val breed: List<String> = emptyList(),
    val size: List<Size> = emptyList(),
    val gender: List<Gender> = emptyList(),
    val age: List<Age> = emptyList(),
    val color: List<String> = emptyList(),
    val coat: List<Coat> = emptyList(),
    val status: List<Status> = listOf(Status.Adoptable),
    val name: String? = null,
    val organization: List<String> = emptyList(),
    val goodWithChildren: Boolean? = null,
    val goodWithDogs: Boolean? = null,
    val goodWithCats: Boolean? = null,
    val houseTrained: Boolean = false,
    val declawed: Boolean = false,
    val specialNeeds: Boolean = false,
    val location: String? = null,
    val distance: Int? = null,
    val before: String? = null,
    val after: String? = null,
    val sort: Sort = Sort.Recent
    )

enum class Size(val value: String) {
    Small("small"),
    Medium("medium"),
    Large("large"),
    ExtraLarge("xlarge")
}

enum class Gender(val value: String) {
    Male("male"),
    Female("female"),
    Unknown("unknown")
}

enum class Age(val value: String) {
    Baby("baby"),
    Young("young"),
    Adult("adult"),
    Senior("senior")
}

enum class Coat(val value: String) {
    Short("short"),
    Medium("medium"),
    Long("long"),
    Wire("wire"),
    Hairless("hairless"),
    Curly("curly")
}

enum class Status(val value: String) {
    Adoptable("adoptable"),
    Adopted("adopted"),
    Found("found")
}

enum class Sort( val value: String) {
    Recent("recent"),
    RevRecent("-recent"),
    Distance("distance"),
    RevDistance("-distance"),
    Random("random")
}