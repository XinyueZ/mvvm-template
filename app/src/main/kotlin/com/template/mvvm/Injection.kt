package com.template.mvvm

import android.app.Application
import com.template.mvvm.data.repository.LicensesDataSource
import com.template.mvvm.data.repository.LicensesRepository
import com.template.mvvm.data.repository.ProductsRepository
import com.template.mvvm.data.repository.cache.LicensesCache
import com.template.mvvm.data.repository.cache.ProductsCache
import com.template.mvvm.data.repository.local.LicensesLocal
import com.template.mvvm.data.repository.local.ProductsLocal
import com.template.mvvm.data.repository.remote.LicensesApi
import com.template.mvvm.data.repository.remote.LicensesRemote
import com.template.mvvm.data.repository.remote.ProductsApi
import com.template.mvvm.data.repository.remote.ProductsRemote
import com.template.mvvm.data.repository.remote.interceptors.NetworkConnectionInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Injection {

    //Provides repository for products
    fun provideProductsRepository() = ProductsRepository(
            provideRemoteProductsRepository(),
            provideLocalProductsRepository(),
            provideCacheProductsRepository()
    )

    private fun provideRemoteProductsRepository() = ProductsRemote()
    private fun provideLocalProductsRepository() = ProductsLocal()
    private fun provideCacheProductsRepository() = ProductsCache()

    //Provides repository for licenses
    fun provideLicensesRepository(application: Application) = LicensesRepository(
            application,
            provideRemoteLicensesRepository(),
            provideLocalLicensesRepository(application),
            provideCacheLicensesRepository()
    )

    private fun provideRemoteLicensesRepository(): LicensesDataSource = LicensesRemote()
    private fun provideLocalLicensesRepository(application: Application) = LicensesLocal(application)
    private fun provideCacheLicensesRepository() = LicensesCache()

    //Provides API
    val networkConnectionInterceptor: Interceptor  = NetworkConnectionInterceptor(App.connectivityManager!!)
    val client: OkHttpClient by lazy { OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build() }

    fun provideProductsApiService() = Retrofit.Builder().client(client).baseUrl("https://api.zalando.com/").addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(ProductsApi::class.java)

    fun provideLicensesApiService() = Retrofit.Builder().client(client).baseUrl("https://dl.dropboxusercontent.com/s/").addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(LicensesApi::class.java)
}