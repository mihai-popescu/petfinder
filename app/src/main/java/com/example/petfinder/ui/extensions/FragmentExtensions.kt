package com.example.petfinder.ui.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> Fragment.stateFlowCollect(
    stateFlow: StateFlow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            stateFlow.collect(block)
        }
    }
}