package com.template.mvvm.source.remote

import com.template.mvvm.feeds.licenses.LicensesData
import com.template.mvvm.feeds.products.BrandsData
import com.template.mvvm.feeds.products.ProductsData
import io.reactivex.Flowable
import retrofit2.http.GET

interface ProductsApi {
    @GET("articles")
    fun getArticles(): Flowable<ProductsData>

    @GET("brands")
    fun getBrands(): Flowable<BrandsData>

    companion object {
        lateinit var service: ProductsApi
    }
}

interface LicensesApi {
    @GET("dxf7rgkcrsezbsw/licenses-list.json")
    fun getLibraries(): Flowable<LicensesData>

    companion object {
        lateinit var service: LicensesApi
    }
}
