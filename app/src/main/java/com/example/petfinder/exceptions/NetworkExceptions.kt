package com.example.petfinder.exceptions

import java.io.IOException

object NoConnectivityException : IOException() {
	override val message: String
		get() = "No network available"
}

object NetworkErrorException : IOException() {
	override val message: String
		get() = "There was a problem with the network"
}

object GeneralException : IOException() {
	override val message: String
		get() = "An error occurred"
}

object AuthorizationException : IOException() {
	override val message: String
		get() = "No AuthorizationFound"
}