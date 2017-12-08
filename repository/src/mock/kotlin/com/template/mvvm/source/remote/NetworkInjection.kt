package com.template.mvvm.source.remote

import android.content.Context
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

internal object NetworkInjection {
    private val behavior = NetworkBehavior.create().apply {
        setErrorPercent(0)
        setFailurePercent(0)
        setVariancePercent(0)
    }

    fun provideProductsApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockProductsApi(context, MockRetrofit.Builder(
                    retrofitBuilder
                            .baseUrl("http://www.hello.com")
                            .build())
                    .networkBehavior(behavior)
                    .build()
                    .create(ProductsApi::class.java))

    fun provideLicensesApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockLicensesApi(context, MockRetrofit.Builder(
                    retrofitBuilder
                            .baseUrl("http://www.hello.com")
                            .build())
                    .networkBehavior(behavior)
                    .build()
                    .create(LicensesApi::class.java))
}