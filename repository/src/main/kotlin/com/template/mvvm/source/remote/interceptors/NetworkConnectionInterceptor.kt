package com.template.mvvm.data.source.remote.interceptors

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val connectivityManager: ConnectivityManager) : Interceptor {

    @SuppressLint("MissingPermission")
    @Throws(MissingNetworkConnectionException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val activeNetwork = connectivityManager.activeNetworkInfo

        if (isConnected(activeNetwork)) {
            throw MissingNetworkConnectionException()
        }

        return chain?.proceed(chain.request())
    }

    private fun isConnected(activeNetwork: NetworkInfo?) =
            (activeNetwork == null) || !activeNetwork.isConnected || ((activeNetwork.type != ConnectivityManager.TYPE_WIFI) && (activeNetwork.type != ConnectivityManager.TYPE_MOBILE))
}