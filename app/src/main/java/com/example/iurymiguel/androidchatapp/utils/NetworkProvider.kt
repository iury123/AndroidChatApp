package com.example.iurymiguel.androidchatapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkProvider(val context: Context) {


    /**
     * Check if the network is available.
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        activeNetworkInfo?.let {
            return it.isConnected
        } ?: return false
    }
}