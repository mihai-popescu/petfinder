package com.example.petfinder.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.petfinder.managers.SearchCriteriaManager
import com.example.petfinder.models.Animal
import com.example.petfinder.models.LocationSearch
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetsListViewModel: ViewModel(), KoinComponent {
    private val searchCriteriaManager: SearchCriteriaManager by inject()

    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 20)

    init {
        val searchCriteria = searchCriteriaManager.searchCriteria
//        searchCriteriaManager.searchCriteria = searchCriteria.copy(location = LocationSearch.SetLocation("New York"))
    }

    @OptIn(ExperimentalPagingApi::class)
    fun fetchPets(): Flow<PagingData<Animal>> {
        val pagingSourceFactory =
            { PetDataSource() }


        return Pager(
            config = pagingConfig,
            initialKey = 1,
            remoteMediator = null,
            pagingSourceFactory = pagingSourceFactory).flow.cachedIn(viewModelScope)
    }

    fun updateLocation(location: Location?) {
        if (location == null || !isAutoLocation()) return
        searchCriteriaManager.searchCriteria = searchCriteriaManager.searchCriteria.copy(location = LocationSearch.AutoLocation("${location.latitude}, ${location.longitude}"))
    }

    fun isAutoLocation() = searchCriteriaManager.searchCriteria.location is LocationSearch.AutoLocation
}