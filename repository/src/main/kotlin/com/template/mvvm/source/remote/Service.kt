package com.template.mvvm.source.remote

import com.template.mvvm.feeds.licenses.LicensesData
import com.template.mvvm.feeds.products.ProductsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val PAGE_SIZE = 50

interface ProductsApi {
    @GET("products?pid=uid4100-40207790-50&limit=$PAGE_SIZE")
    fun getArticles(): Call<ProductsData>

    @GET("products?pid=uid4100-40207790-50&limit=$PAGE_SIZE")
    fun filterArticles(@Query("cat") keyword: String): Call<ProductsData>

    companion object {
        lateinit var service: ProductsApi
    }
}

interface LicensesApi {
    @GET("dxf7rgkcrsezbsw/licenses-list.json")
    fun getLibraries(): Call<LicensesData>

    companion object {
        lateinit var service: LicensesApi
    }
}
