package com.example.petfinder.models.network

data class TypeResponse(val name: String?)

data class TypesCallResponse(val types: List<TypeResponse>)