package com.example.book_hub.util

import android.content.Context
import android.net.NetworkInfo
import android.net.ConnectivityManager

class ConnectionManager {

    fun checkConnectivity(context : Context) : Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworks : NetworkInfo? = connectivityManager.activeNetworkInfo

        if (activeNetworks?.isConnected != null){
            return activeNetworks.isConnected
        }else{
            return false
        }
    }
}