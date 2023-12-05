package com.example.petfinder.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    val id: Long,
    val organizationId: String,
    val breeds: Breeds,
    val gender: String,
    val size: String,
    val name: String,
    val photos: List<Photo>,
    val status: String,
    val distance: Double
): Parcelable

@Parcelize
data class Breeds(
    val primary: String,
    val secondary: String,
    val isMixed: Boolean,
    val isUnknown: Boolean
): Parcelable

@Parcelize
data class Photo(
    val small: String,
    val medium: String,
    val large: String,
    val full: String
): Parcelable