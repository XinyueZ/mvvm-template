package com.template.mvvm.source.remote

import android.content.Context
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

internal fun NetworkBehavior.setNetworkErrorPercent(percent: Int) {
    setErrorPercent(percent)
    setFailurePercent(percent)
    setVariancePercent(percent)
}

internal object NetworkInjection {
    internal val behavior: NetworkBehavior  by lazy {
        NetworkBehavior.create().apply {
            setNetworkErrorPercent(0)
        }
    }

    fun provideProductsApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockProductsApi(
                    context,
                    MockRetrofit.Builder(
                            retrofitBuilder
                                    .baseUrl("http://www.hello.com")
                                    .build())
                            .networkBehavior(behavior)
                            .build()
                            .create(ProductsApi::class.java)
            )

    fun provideLicensesApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockLicensesApi(
                    context,
                    MockRetrofit.Builder(
                            retrofitBuilder
                                    .baseUrl("http://www.hello.com")
                                    .build())
                            .networkBehavior(behavior)
                            .build()
                            .create(LicensesApi::class.java)
            )
}