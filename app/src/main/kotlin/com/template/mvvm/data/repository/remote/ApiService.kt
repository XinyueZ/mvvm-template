package com.template.mvvm.data.repository.remote

import com.template.mvvm.data.feeds.products.ProductsData
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET

interface ProductsApi {
    @GET("articles")
    fun getArticles(): Single<ProductsData>

    companion object {
        val service =
                Retrofit.Builder().baseUrl("https://api.zalando.com/").build().create(ProductsApi::class.java)
    }
}
