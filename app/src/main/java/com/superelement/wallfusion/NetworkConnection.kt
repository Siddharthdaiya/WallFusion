package com.superelement.wallfusion
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

class NetworkConnection(private val context: Context) {

    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var networkStateListener: NetworkStateListener? = null

    interface NetworkStateListener {
        fun onNetworkAvailable()
        fun onNetworkLost()
    }

    fun setNetworkStateListener(listener: NetworkStateListener) {
        this.networkStateListener = listener
    }

    fun registerNetworkCallback() {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    networkStateListener?.onNetworkAvailable()
                }

                override fun onLost(network: Network) {
                    networkStateListener?.onNetworkLost()
                }
            }
            connectivityManager?.registerDefaultNetworkCallback(networkCallback!!)
        }
    }

    fun unregisterNetworkCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback?.let {
                try {
                    connectivityManager?.unregisterNetworkCallback(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                networkCallback = null
            }
        }
    }
}
