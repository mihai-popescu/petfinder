package com.example.petfinder.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.petfinder.models.Animal

interface PetSourceAdapterListener {
    fun onClick(animal: Animal)
}

class PetSourceAdapter(
    private val listener: PetSourceAdapterListener?
) : PagingDataAdapter<Animal, PetItemViewHolder>(PET_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetItemViewHolder {
        return PetItemViewHolder.create(parent, object : PetItemViewHolderListener {
            override fun onClick(animal: Animal) {
                listener?.onClick(animal)
            }
        })
    }

    override fun onBindViewHolder(holder: PetItemViewHolder, position: Int) {
        getItem(position)?.let { animal ->
            holder.bind(animal)
        }
    }

    companion object {
        val PET_COMPARATOR = object : DiffUtil.ItemCallback<Animal>() {
            override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean =
                oldItem == newItem
        }
    }
}
