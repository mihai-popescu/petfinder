package com.example.petfinder.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.petfinder.databinding.FragmentFilterBinding
import com.example.petfinder.models.Size
import com.example.petfinder.ui.extensions.stateFlowCollect
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment: Fragment() {
    private val viewModel: FilterViewModel by viewModel()

    private var _binding: FragmentFilterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("MissingPermission")
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        with(binding)  {
            sizeContainer.addView(CheckBox(this@FilterFragment.requireContext()).apply {
                text = Size.Small.value
                isChecked = viewModel.isSmall()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setSize(Size.Small, isChecked)
                }
            })
            sizeContainer.addView(CheckBox(this@FilterFragment.requireContext()).apply {
                text = Size.Medium.value
                isChecked = viewModel.isMedium()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setSize(Size.Medium, isChecked)
                }
            })
            sizeContainer.addView(CheckBox(this@FilterFragment.requireContext()).apply {
                text = Size.Large.value
                isChecked = viewModel.isLarge()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setSize(Size.Large, isChecked)
                }
            })
            sizeContainer.addView(CheckBox(this@FilterFragment.requireContext()).apply {
                text = Size.ExtraLarge.value
                isChecked = viewModel.isExtraLarge()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setSize(Size.ExtraLarge, isChecked)
                }
            })
            when {
                viewModel.isLocationNewYork() -> radioLocationSet.isChecked = true
                viewModel.isLocationAuto() -> radioLocationAuto.isChecked = true
                else -> radioLocationNone.isChecked = true
            }
            radioLocationSet.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                    viewModel.setNewYorkLocation(true)
            }
            radioLocationAuto.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.setAutoLocation(true)
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
                    }
                }
            }
            radioLocationNone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                    viewModel.setNewYorkLocation(false)
            }

            btnReset.setOnClickListener {
                sizeContainer.children.filterIsInstance(CheckBox::class.java).iterator().forEach {
                    it.isChecked = false
                }
                radioLocationNone.isChecked = true
                viewModel.reset()
            }
        }
    }

    private fun setupObservers() {
        stateFlowCollect(viewModel.types) {
            if (it.isNotEmpty()) {
                binding.typeGroup.removeAllViews()
                binding.typeGroup.addView(RadioButton(this@FilterFragment.requireContext()).apply {
                    text = "Any"
                    setOnCheckedChangeListener { _, isChecked ->
                        viewModel.setType(null)
                    }
                })
                it.forEach { type ->
                    binding.typeGroup.addView(RadioButton(this@FilterFragment.requireContext()).apply {
                        text = type.name
                        setOnCheckedChangeListener { _, isChecked ->
                            viewModel.setType(type.name)
                        }
                    })
                }
                binding.typeGroup.children.iterator().forEach {
                    (it as? RadioButton)?.let { radioButton ->
                        val label = if (radioButton.text.toString() != "Any") radioButton.text.toString() else null
                        if (viewModel.isType(label))
                            radioButton.isChecked = true
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}