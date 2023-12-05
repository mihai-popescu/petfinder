package com.example.petfinder

import com.example.petfinder.managers.NetworkManager
import com.example.petfinder.managers.SearchCriteriaManager
import com.example.petfinder.managers.SharedPreferencesManager
import com.example.petfinder.managers.SharedPreferencesManagerImpl
import com.example.petfinder.models.Animal
import com.example.petfinder.network.PetFinderService
import com.example.petfinder.network.AuthorizedTokenProvider
import com.example.petfinder.network.PetFinderRetrofitProvider
import com.example.petfinder.network.UnauthorizedService
import com.example.petfinder.repositories.PetRepository
import com.example.petfinder.ui.FilterViewModel
import com.example.petfinder.ui.PetDetailsViewModel
import com.example.petfinder.ui.PetsListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PetFinderModule = module {
    single { NetworkManager(androidApplication()) }
    single { PetFinderRetrofitProvider.unauthorized().create(UnauthorizedService::class.java) }
    single { PetFinderRetrofitProvider.authorized().create(PetFinderService::class.java) }
    single { AuthorizedTokenProvider() }
    single { PetRepository() }
    single<SharedPreferencesManager> { SharedPreferencesManagerImpl(androidApplication()) }
    single { SearchCriteriaManager() }

    viewModel { PetsListViewModel() }
    viewModel { (animal: Animal) -> PetDetailsViewModel(animal) }
    viewModel { FilterViewModel() }
}