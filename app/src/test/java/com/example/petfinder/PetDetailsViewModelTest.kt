package com.example.petfinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.petfinder.models.Animal
import com.example.petfinder.models.Breeds
import com.example.petfinder.models.Gender
import com.example.petfinder.models.Photo
import com.example.petfinder.models.Size
import com.example.petfinder.models.Status
import com.example.petfinder.network.PetFinderService
import com.example.petfinder.ui.PetDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito

class PetDetailsViewModelTest: KoinTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { declareMock<PetFinderService>() }
                single { PetDetailsViewModel(animal) }
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


    @Test
    fun composeWithLessData() {
        val viewModel = PetDetailsViewModel(animal)
        val update = Animal(
        id = 321,
        organizationId = "",
        breeds = Breeds("", "", false, true),
        gender = Gender.Unknown.value,
        size = Size.Medium.value,
        name = "",
        photos = listOf(Photo("", "", "", "")),
        status = Status.Adoptable.value,
        distance = .0
        )
        val newValues = viewModel.composeAnimalData(animal, update)
        assertEquals(animal, newValues)
    }

    @Test
    fun composeWithNewData() {
        val viewModel = PetDetailsViewModel(animal)
        val update = Animal(
            id = 321,
            organizationId = "organizationId",
            breeds = Breeds("Cicas", "", false, true),
            gender = Gender.Male.value,
            size = Size.ExtraLarge.value,
            name = "name",
            photos = listOf(Photo("small", "", "", "Full")),
            status = Status.Adoptable.value,
            distance = 45.32
        )
        val newValues = viewModel.composeAnimalData(animal, update)
        assertEquals(update, newValues)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }
}