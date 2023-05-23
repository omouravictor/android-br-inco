package com.omouravictor.ratesbr.network

import android.net.ConnectivityManager
import android.net.Network
import android.os.Handler
import android.os.Looper

class ConnectivityObserver(
    private val callbackFunction: () -> Unit
) : ConnectivityManager.NetworkCallback() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onAvailable(network: Network) {
        handler.post {
            callbackFunction()
        }
    }
}