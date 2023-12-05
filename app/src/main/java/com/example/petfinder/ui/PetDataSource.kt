package com.example.petfinder.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.petfinder.exceptions.AuthorizationException
import com.example.petfinder.exceptions.GeneralException
import com.example.petfinder.exceptions.NetworkErrorException
import com.example.petfinder.exceptions.NoConnectivityException
import com.example.petfinder.models.Animal
import com.example.petfinder.repositories.PetRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import com.example.petfinder.repositories.Result

class PetDataSource: PagingSource<Int, Animal>(), KoinComponent {
    private val repository: PetRepository by inject()

    override fun getRefreshKey(state: PagingState<Int, Animal>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Animal> {
        return try {
            val pageNumber = params.key ?: 0
            val itemList: MutableList<Animal> = mutableListOf()

            when (val result =
                repository.search(pageNumber)) {
                is Result.Success -> {
                    itemList.addAll(result.data)
                }
                is Result.AuthorizationNotFoundError -> throw AuthorizationException
                is Result.Error -> throw GeneralException
                is Result.NetworkError -> throw NetworkErrorException
                is Result.NoConnectionError -> throw NoConnectivityException
            }
            val prevKey = if (pageNumber > 1) pageNumber - 1 else null

            val nextKey = if (itemList.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(data = itemList.toList(), prevKey = prevKey, nextKey = nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

}