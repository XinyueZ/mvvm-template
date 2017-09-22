package com.template.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.grapesnberries.curllogger.CurlLoggerInterceptor
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.data.source.LicensesRepository
import com.template.mvvm.data.source.ProductsRepository
import com.template.mvvm.data.source.Repository
import com.template.mvvm.data.source.cache.LicensesCache
import com.template.mvvm.data.source.cache.ProductsCache
import com.template.mvvm.data.source.local.LicensesLocal
import com.template.mvvm.data.source.local.ProductsLocal
import com.template.mvvm.data.source.remote.LicensesApi
import com.template.mvvm.data.source.remote.LicensesRemote
import com.template.mvvm.data.source.remote.ProductsApi
import com.template.mvvm.data.source.remote.ProductsRemote
import com.template.mvvm.data.source.remote.interceptors.NetworkConnectionInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class Injection internal constructor(application: Application) {
    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: Injection? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(Injection::class.java) {
                    INSTANCE ?: Injection(application)
                            .also { INSTANCE = it }
                }

        fun destroyInstance() {
            INSTANCE?.let {
                it.DS_INSTANCE?.clear()
                it.DS_INSTANCE = null
                INSTANCE = null
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Volatile private var DS_INSTANCE: Repository? = null

    // Provides whole repository
    fun provideRepository(application: Application) = DS_INSTANCE ?: synchronized(this) {
        DS_INSTANCE ?: Repository(provideLicensesRepository(application), provideProductsRepository())
                .also { DS_INSTANCE = it }
    }

    // Provides repository for products
    private fun provideProductsRepository() = ProductsRepository(
            provideRemoteProductsRepository(),
            provideLocalProductsRepository(),
            provideCacheProductsRepository()
    )

    private fun provideRemoteProductsRepository() = ProductsRemote()
    private fun provideLocalProductsRepository() = ProductsLocal()
    private fun provideCacheProductsRepository() = ProductsCache()

    // Provides repository for licenses
    private fun provideLicensesRepository(application: Application) = LicensesRepository(
            application,
            provideRemoteLicensesRepository(),
            provideLocalLicensesRepository(application),
            provideCacheLicensesRepository()
    )

    private fun provideRemoteLicensesRepository(): LicensesDataSource = LicensesRemote()
    private fun provideLocalLicensesRepository(application: Application) = LicensesLocal(application)
    private fun provideCacheLicensesRepository() = LicensesCache()

    //
    // Provides API: Interceptors, client, providers of APIs
    //
    // Interceptors
    private val networkConnectionInterceptor: Interceptor = NetworkConnectionInterceptor(application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    // Http-Client
    private val client: OkHttpClient by lazy { OkHttpClient.Builder().addInterceptor(CurlLoggerInterceptor("#!#!")).addInterceptor(networkConnectionInterceptor).build() }
    private val gsonFactory: GsonConverterFactory by lazy {
        GsonConverterFactory.create(
                GsonBuilder()
                        .registerTypeAdapter(Uri::class.java, UriAdapter)
                        .create()
        )
    }

    fun provideProductsApiService() = Retrofit.Builder().client(client).baseUrl("https://api.zalando.com/").addConverterFactory(gsonFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(ProductsApi::class.java)

    fun provideLicensesApiService() = Retrofit.Builder().client(client).baseUrl("https://dl.dropboxusercontent.com/s/").addConverterFactory(gsonFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(LicensesApi::class.java)
}

private object UriAdapter : JsonDeserializer<Uri> {
    @Throws(exceptionClasses = *arrayOf(Exception::class))
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Uri {
        if (json == null) {
            throw IllegalArgumentException("Deserialization failed: null")
        }

        val jsObject = json.asString
        return Uri.parse(jsObject)
    }
}