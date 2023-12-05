package com.example.petfinder.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.petfinder.databinding.FragmentPetDetailsBinding
import com.example.petfinder.models.Animal
import com.example.petfinder.ui.extensions.stateFlowCollect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PetDetailsFragment : Fragment() {
    private val navArgs: PetDetailsFragmentArgs by navArgs()
    private val viewModel: PetDetailsViewModel by viewModel {
        parametersOf(navArgs.animal)
    }

    private var _binding: FragmentPetDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPetDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() = Unit

    private fun setupObservers() {
        stateFlowCollect(viewModel.animal) {
            updateAnimal(it)
        }
    }

    private fun updateAnimal(animal: Animal) {
        with (binding) {
            petItemThumbnail.load(animal.photos.firstOrNull()?.full)
            textViewName.text = animal.name
            textViewBreed.text = animal.breeds.primary
            textViewSize.text = animal.size
            textViewGender.text = animal.gender
            textViewStatus.text = animal.status
            textViewDistance.text = animal.distance.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}