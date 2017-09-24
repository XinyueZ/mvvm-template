package com.template.mvvm.source.remote

import com.template.mvvm.feeds.licenses.LicensesData
import com.template.mvvm.feeds.products.ProductsData
import io.reactivex.Single
import retrofit2.http.GET

interface ProductsApi {
    @GET("articles")
    fun getArticles(): Single<ProductsData>

    companion object {
         var service: ProductsApi? = null
    }
}


interface LicensesApi {
    @GET("dxf7rgkcrsezbsw/licenses-list.json")
    fun getLibraries(): Single<LicensesData>

    companion object {
        var service: LicensesApi? = null
    }
}
