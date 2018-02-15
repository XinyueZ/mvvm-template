package com.template.mvvm.repository.source.remote

import android.content.Context
import retrofit2.Retrofit

internal object NetworkInjection {
    fun provideProductsApiService(cxt: Context, retrofitBuilder: Retrofit.Builder) =
            retrofitBuilder
                    .baseUrl("https://api.shopstyle.com/api/v2/")
                    .build()
                    .create(ProductsApi::class.java)

    fun provideLicensesApiService(cxt: Context, retrofitBuilder: Retrofit.Builder) =
            retrofitBuilder
                    .baseUrl("https://dl.dropboxusercontent.com/s/")
                    .build()
                    .create(LicensesApi::class.java)
}