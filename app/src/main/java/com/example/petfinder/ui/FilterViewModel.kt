package com.example.petfinder.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinder.managers.SearchCriteriaManager
import com.example.petfinder.models.AnimalType
import com.example.petfinder.models.LocationSearch
import com.example.petfinder.models.SearchCriteria
import com.example.petfinder.models.Size
import com.example.petfinder.models.network.ResultWrapper
import com.example.petfinder.models.network.safeApiCall
import com.example.petfinder.network.PetFinderService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FilterViewModel: ViewModel(), KoinComponent {
    private val searchCriteriaManager: SearchCriteriaManager by inject()
    private val service: PetFinderService by inject()
    private val _types = MutableStateFlow(emptyList<AnimalType>())
    val types = _types.asStateFlow()

    init {
        viewModelScope.launch {
            when (val response = safeApiCall(IO) {
                service.getTypes()
            }) {
                is ResultWrapper.Success -> _types.value =
                    response.getSuccessValue()?.types?.mapNotNull { it.name?.let { AnimalType(it) } }
                        ?: emptyList()
                else -> Log.i("FilterViewModel", "Handle error")
            }
        }
    }

    fun isLocationNewYork(): Boolean {
        return searchCriteriaManager.searchCriteria.location is LocationSearch.SetLocation
    }

    fun isLocationAuto(): Boolean = searchCriteriaManager.searchCriteria.location is LocationSearch.AutoLocation

    fun isSmall(): Boolean {
        return searchCriteriaManager.searchCriteria.size.contains(Size.Small)
    }

    fun isMedium(): Boolean {
        return searchCriteriaManager.searchCriteria.size.contains(Size.Medium)
    }

    fun isLarge(): Boolean {
        return searchCriteriaManager.searchCriteria.size.contains(Size.Large)
    }

    fun isExtraLarge(): Boolean {
        return searchCriteriaManager.searchCriteria.size.contains(Size.ExtraLarge)
    }

    fun setNewYorkLocation(value: Boolean) {
        searchCriteriaManager.searchCriteria.let {
            searchCriteriaManager.searchCriteria = it.copy(location = if (value) LocationSearch.SetLocation("New York") else LocationSearch.None)
        }
    }

    fun setAutoLocation(value: Boolean) {
        searchCriteriaManager.searchCriteria.let {
            searchCriteriaManager.searchCriteria = it.copy(location = if (value) LocationSearch.AutoLocation("0, 0") else LocationSearch.None)
        }
    }

    fun setSize(size: Size, value: Boolean) {
        searchCriteriaManager.searchCriteria.let {
            searchCriteriaManager.searchCriteria = it.copy(size = if (value) it.size + size else it.size - size)
        }
    }
    fun reset() {
        searchCriteriaManager.searchCriteria = SearchCriteria()
    }

    fun isType(type:String?) =
        searchCriteriaManager.searchCriteria.type == type

    fun setType(type: String?) {
        searchCriteriaManager.searchCriteria = searchCriteriaManager.searchCriteria.copy(type = type)
    }
}