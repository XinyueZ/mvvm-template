package com.template.mvvm

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.grapesnberries.curllogger.CurlLoggerInterceptor
import com.template.mvvm.source.local.DB
import com.template.mvvm.source.local.DatabaseInjection.provideDatabase
import com.template.mvvm.source.remote.LicensesApi
import com.template.mvvm.source.remote.NetworkInjection.provideLicensesApiService
import com.template.mvvm.source.remote.NetworkInjection.provideProductsApiService
import com.template.mvvm.source.remote.ProductsApi
import com.template.mvvm.source.remote.interceptors.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryModule(context: Context) {
    private var activeDebugTool = false

    public fun shouldUseDebugTool() = BuildConfig.DEBUG && TextUtils.equals(BuildConfig.FLAVOR, "prod")

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
                .client(
                        OkHttpClient.Builder()
                                .addDebugInterceptors()
                                .addInterceptor(NetworkConnectionInterceptor(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)).build()
                )
                .addConverterFactory(
                        GsonConverterFactory.create(
                                GsonBuilder()
                                        .create()
                        )
                )
    }

    private fun OkHttpClient.Builder.addDebugInterceptors(): OkHttpClient.Builder {
        if (shouldUseDebugTool()) {
            addNetworkInterceptor(StethoInterceptor())
            addInterceptor(CurlLoggerInterceptor("#!#!"))
            activeDebugTool = true
        }
        return this
    }

    init {
        onCreate(context)
    }

    private fun onCreate(context: Context) {
        if (shouldUseDebugTool()) {
            Stetho.initializeWithDefaults(context)
            activeDebugTool = true
        }
        DB.INSTANCE = provideDatabase(context)
        ProductsApi.service = provideProductsApiService(context, retrofitBuilder)
        LicensesApi.service = provideLicensesApiService(context, retrofitBuilder)
    }

}