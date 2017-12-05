package com.template.mvvm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
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

class RepositoryModule(application: Application) {
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
                .client(
                        OkHttpClient.Builder()
                                .addNetworkInterceptor(StethoInterceptor())
                                .addInterceptor(CurlLoggerInterceptor("#!#!"))
                                .addInterceptor(NetworkConnectionInterceptor(application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)).build()
                )
                .addConverterFactory(
                        GsonConverterFactory.create(
                                GsonBuilder()
                                        .create()
                        )
                )
    }

    init {
        onCreate(application)
    }

    private fun onCreate(application: Application) {
        Stetho.initializeWithDefaults(application)
        DB.INSTANCE = provideDatabase(application)
        ProductsApi.service = provideProductsApiService(application, retrofitBuilder)
        LicensesApi.service = provideLicensesApiService(application, retrofitBuilder)
    }
}