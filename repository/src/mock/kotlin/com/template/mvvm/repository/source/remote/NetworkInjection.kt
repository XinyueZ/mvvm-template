package com.template.mvvm.repository.source.remote

import android.content.Context
import com.template.mvvm.repository.RepositoryInjection
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit

internal object NetworkInjection {

    fun provideProductsApiService(context: Context, retrofitBuilder: Retrofit.Builder) =
            MockProductsApi(
                    context,
                    MockRetrofit.Builder(
                            retrofitBuilder
                                    .baseUrl("http://www.hello.com")
                                    .build())
                            .networkBehavior(RepositoryInjection.networkBehavior)
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
                            .networkBehavior(RepositoryInjection.networkBehavior)
                            .build()
                            .create(LicensesApi::class.java)
            )
}