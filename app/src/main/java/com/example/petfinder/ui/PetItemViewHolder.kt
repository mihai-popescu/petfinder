package com.example.petfinder.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.petfinder.databinding.PetItemBinding
import com.example.petfinder.models.Animal

interface PetItemViewHolderListener {
    fun onClick(animal: Animal)
}

class PetItemViewHolder(binding: PetItemBinding, private val listener: PetItemViewHolderListener?): RecyclerView.ViewHolder(binding.root) {
    private val thumbnail: ImageView = binding.petItemThumbnail
    private val name: TextView = binding.petItemName
    private var animal: Animal? = null

    init {
        binding.root.setOnClickListener {
            animal?.let { listener?.onClick(it) }
        }
    }

    fun bind(animal: Animal) {
       this.animal = animal
       this.name.text = animal.name
        animal.photos.firstOrNull()?.small?.let {
            this.thumbnail.load(it)
        } ?: this.thumbnail.load(null)
    }

    companion object {
        fun create(parent: ViewGroup, listener: PetItemViewHolderListener?): PetItemViewHolder {
            val binding = PetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PetItemViewHolder(binding, listener)
        }
    }
}