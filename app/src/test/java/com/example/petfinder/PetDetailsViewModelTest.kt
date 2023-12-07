package com.example.petfinder

import com.example.petfinder.exceptions.AuthorizationException
import com.example.petfinder.models.Animal
import com.example.petfinder.models.Breeds
import com.example.petfinder.models.Gender
import com.example.petfinder.models.Photo
import com.example.petfinder.models.Size
import com.example.petfinder.models.Status
import com.example.petfinder.models.network.AnimalCallResponse
import com.example.petfinder.models.network.AnimalResponse
import com.example.petfinder.models.network.BreedsResponse
import com.example.petfinder.models.network.ColorsResponse
import com.example.petfinder.models.network.PhotoResponse
import com.example.petfinder.models.network.ResultWrapper
import com.example.petfinder.network.AuthorizedTokenProvider
import com.example.petfinder.network.PetFinderService
import com.example.petfinder.ui.PetDetailsViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response

class PetDetailsViewModelTest: KoinTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { PetDetailsViewModel(animal) }
                declareMock<AuthorizedTokenProvider> { AuthorizedTokenProvider() }
            })
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    private val animal = Animal(
            id = 321,
            organizationId = "organizationId",
            breeds = Breeds("primary_breed", "", true, false),
            gender = Gender.Female.value,
            size = Size.Large.value,
            name = "name",
            photos = listOf(Photo("small", "", "", "Full")),
            status = Status.Adoptable.value,
            distance = 123.0
        )

    private val originalAnimalResponse = AnimalCallResponse(
        AnimalResponse(
            id = 321,
            organizationId = "organizationId",
            url = null,
            type = null,
            species = null,
            breeds = BreedsResponse("primary_breed", "", true, false),
            colors = ColorsResponse(null, null, null),
            age = null,
            gender = Gender.Female.value,
            size = Size.Large.value,
            coat = null,
            name = "name",
            description = "",
            photos = listOf(PhotoResponse("small", "", "", "Full")),
            videos = emptyList(),
            status = Status.Adoptable.value,
            attributes = null,
            environment = null,
            tags = null,
            contact = null,
            publishedAt = null,
            distance = 123.0,
            links = null
        )
    )

    private val updatedAnimalResponse = AnimalCallResponse(
        AnimalResponse(
            id = 321,
            organizationId = "organizationId",
            url = null,
            type = null,
            species = null,
            breeds = BreedsResponse("primary_breed", "", true, false),
            colors = ColorsResponse(null, null, null),
            age = null,
            gender = Gender.Female.value,
            size = Size.Large.value,
            coat = null,
            name = "name",
            description = "",
            photos = listOf(PhotoResponse("small-improved", "", "", "Full-improved")),
            videos = emptyList(),
            status = Status.Adoptable.value,
            attributes = null,
            environment = null,
            tags = null,
            contact = null,
            publishedAt = null,
            distance = 122.45,
            links = null
        )
    )

    val error = """{
    "type": "https://www.petfinder.com/developers/v2/docs/errors/ERR-00001/",
    "status": 401,
    "title": "Invalid Request",
    "detail": "Access token invalid or expired"
}
"""


    @Test
    fun composeWithLessData() {
        val petFinderService = declareMock<PetFinderService>()
        runBlocking {
            Mockito.`when`(petFinderService.getAnimal(animal.id.toInt())).thenReturn(originalAnimalResponse)
        }
        val viewModel = PetDetailsViewModel(animal)
        val newValues = viewModel.animal.value
        assertEquals(animal, newValues)
    }

    @Test
    fun composeWithNewData() {
        val petFinderService = declareMock<PetFinderService>()
        runBlocking {
            Mockito.`when`(petFinderService. getAnimal(animal.id.toInt())).thenReturn(updatedAnimalResponse)
        }
        val viewModel = PetDetailsViewModel(animal)
        val expectedData = Animal(
            id = 321,
            organizationId = "organizationId",
            breeds = Breeds("primary_breed", "", true, false),
            gender = Gender.Female.value,
            size = Size.Large.value,
            name = "name",
            photos = listOf(Photo("small-improved", "", "", "Full-improved")),
            status = Status.Adoptable.value,
            distance = 122.45,
        )
        Thread.sleep(200)
        val newValues = viewModel.animal.value
        assertEquals(expectedData, newValues)
    }



    @Test
    fun authorizationError() {
        val petFinderService = declareMock<PetFinderService>()
        runBlocking {
            Mockito.`when`(petFinderService. getAnimal(animal.id.toInt())).thenAnswer {
                throw HttpException(Response.error<HttpException>(401, error.toResponseBody()))
            }
        }
        val viewModel = PetDetailsViewModel(animal)

        Thread.sleep(200)
        val newValues = viewModel.animal.value
        assertEquals(animal, newValues)
        assertEquals(viewModel.error.value, ResultWrapper.AuthorizationNotFoundError.exception)
    }
}