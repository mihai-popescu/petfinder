package com.example.petfinder.ui

import androidx.lifecycle.ViewModel
import com.example.petfinder.managers.SearchCriteriaManager
import com.example.petfinder.models.SearchCriteria
import com.example.petfinder.models.Size
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FilterViewModel: ViewModel(), KoinComponent {
    private val searchCriteriaManager: SearchCriteriaManager by inject()

    fun isLocationNewYork(): Boolean {
        return searchCriteriaManager.searchCriteria.location == "New York"
    }

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
            searchCriteriaManager.searchCriteria = it.copy(location = if (value) "New York" else null)
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
}