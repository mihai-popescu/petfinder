package com.example.petfinder.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class NetworkState(val isConnected: Boolean) {
	CONNECTED(true),
	DISCONNECTED(false),
	UNINITIALIZED(false)
}

class NetworkManager(context: Context) {
	fun registerCallback() {
		connectivityManager.registerDefaultNetworkCallback(networkCallback)
	}

	private var connectivityManager: ConnectivityManager =
		context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

	private val networkCallback = object : ConnectivityManager.NetworkCallback() {
		override fun onAvailable(network: Network) {
			super.onAvailable(network)
			_networkState.value = NetworkState.CONNECTED
		}

		override fun onLost(network: Network) {
			super.onLost(network)
			_networkState.value = NetworkState.DISCONNECTED
		}
	}

	private val _networkState = MutableStateFlow(NetworkState.UNINITIALIZED)
	val networkState: StateFlow<NetworkState> = _networkState
	val isNetworkConnectionAvailable: Boolean get() = _networkState.value == NetworkState.CONNECTED
}