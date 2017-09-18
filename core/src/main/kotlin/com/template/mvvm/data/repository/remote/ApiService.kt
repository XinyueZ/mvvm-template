package com.template.mvvm.data.repository.remote

import com.template.mvvm.Injection
import com.template.mvvm.data.feeds.licenses.LicensesData
import com.template.mvvm.data.feeds.products.ProductsData
import io.reactivex.Single
import retrofit2.http.GET

interface ProductsApi {
    @GET("articles")
    fun getArticles(): Single<ProductsData>

    companion object {
        val service = Injection.provideProductsApiService()
    }
}


interface LicensesApi {
    @GET("dxf7rgkcrsezbsw/licenses-list.json")
    fun getLibraries(): Single<LicensesData>

    companion object {
        val service = Injection.provideLicensesApiService()
    }
}
