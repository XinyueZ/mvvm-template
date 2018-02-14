package com.template.mvvm.repository.source.remote

import com.template.mvvm.repository.feeds.licenses.LicensesData
import com.template.mvvm.repository.feeds.products.ProductsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {
    @GET("products?pid=uid4100-40207790-50&limit=10&fts=man&fts=women")
    fun getArticles(@Query("offset") offset: Int): Call<ProductsData>

    @GET("products?pid=uid4100-40207790-50&fts=clothes&dress&limit=10")
    fun filterArticles(@Query("offset") offset: Int, @Query("cat") keyword: String): Call<ProductsData>

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
