package com.template.mvvm.data.repository.remote

import com.template.mvvm.data.feeds.licenses.LicensesData
import com.template.mvvm.data.feeds.products.ProductsData
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProductsApi {
    @GET("articles")
    fun getArticles(): Single<ProductsData>

    companion object {
        val service =
                Retrofit.Builder().baseUrl("https://api.zalando.com/").addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(ProductsApi::class.java)
    }
}


interface LicensesApi {
    @GET("dxf7rgkcrsezbsw/licenses-list.json")
    fun getLibraries(): Single<LicensesData>

    companion object {
        val service =
                Retrofit.Builder().baseUrl("https://dl.dropboxusercontent.com/s/").addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(LicensesApi::class.java)
    }
}
