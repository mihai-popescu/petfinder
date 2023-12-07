package com.example.petfinder.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.example.petfinder.databinding.FragmentPetsListBinding
import com.example.petfinder.models.Animal
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PetsListFragment : Fragment() {
    private val viewModel: PetsListViewModel by viewModel()

    private var _binding: FragmentPetsListBinding? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sourceAdapter: PetSourceAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    @SuppressLint("MissingPermission")
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        viewModel.updateLocation(location)
                    }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isAutoLocation())
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                return
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    viewModel.updateLocation(location)
                }

    }

    private fun setupViews() {
        initAdapter()
        with (binding) {
            recyclerView.apply {
                adapter = sourceAdapter
            }
            swipeRefresh.setOnRefreshListener {
                sourceAdapter.submitData(lifecycle, PagingData.empty())
                lifecycleScope.launch {
                    viewModel.fetchPets().collectLatest {
                        swipeRefresh.isRefreshing = false
                        submitSourceData(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.fetchPets().collectLatest {
                submitSourceData(it)
            }
        }
    }

    private fun setupObservers() = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        sourceAdapter = PetSourceAdapter(object : PetSourceAdapterListener {
            override fun onClick(animal: Animal) {
                navigateTo(PetsListFragmentDirections.actionPetListFragmentToPetDetailsFragment(animal.name, animal))
            }
        })
    }

    private suspend fun submitSourceData(data: PagingData<Animal>) {
        sourceAdapter.submitData(data)
    }

    fun navigateTo(directions: NavDirections) {
        with(findNavController()) {
            currentDestination?.getAction(directions.actionId)?.let {
                navigate(directions)
            }
        }
    }

}