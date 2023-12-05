package com.example.petfinder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.petfinder.databinding.FragmentFilterBinding
import com.example.petfinder.models.Size
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment: Fragment() {
    private val viewModel: FilterViewModel by viewModel()

    private var _binding: FragmentFilterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
            materialSwitch.isChecked = viewModel.isLocationNewYork()
            materialSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setNewYorkLocation(
                    isChecked
                )
            }

            btnReset.setOnClickListener {
                sizeContainer.children.filterIsInstance(CheckBox::class.java).iterator().forEach {
                    it.isChecked = false
                }
                materialSwitch.isChecked = false
                viewModel.reset()
            }
        }
    }

    private fun setupObservers()  = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}