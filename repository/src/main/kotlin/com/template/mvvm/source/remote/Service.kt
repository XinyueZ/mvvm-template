package com.template.mvvm.source.remote

import com.template.mvvm.feeds.licenses.LicensesData
import com.template.mvvm.feeds.products.BrandsData
import com.template.mvvm.feeds.products.ProductsData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val PAGE_SIZE = 100
interface ProductsApi {
    @GET("articles?pageSize=$PAGE_SIZE")
    fun getArticles(): Single<ProductsData>

    @GET("articles?pageSize=$PAGE_SIZE")
    fun filterArticles(@Query("gender") keyword: String): Single<ProductsData>

    @GET("brands?pageSize=$PAGE_SIZE")
    fun getBrands(): Single<BrandsData>

    companion object {
        lateinit var service: ProductsApi
    }
}

interface LicensesApi {
    @GET("dxf7rgkcrsezbsw/licenses-list.json")
    fun getLibraries(): Single<LicensesData>

    companion object {
        lateinit var service: LicensesApi
    }
}
