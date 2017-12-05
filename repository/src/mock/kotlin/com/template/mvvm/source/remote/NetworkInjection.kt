package com.template.mvvm.source.remote

import android.content.Context
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit

internal object NetworkInjection {
    fun provideProductsApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockProductsApi(context, MockRetrofit.Builder(
                    retrofitBuilder
                            .baseUrl("http://www.hello.com")
                            .build())
                    .build()
                    .create(ProductsApi::class.java))

    fun provideLicensesApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockLicensesApi(context, MockRetrofit.Builder(
                    retrofitBuilder
                            .baseUrl("http://www.hello.com")
                            .build())
                    .build()
                    .create(LicensesApi::class.java))
}