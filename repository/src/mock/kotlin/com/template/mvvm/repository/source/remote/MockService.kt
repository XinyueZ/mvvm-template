package com.template.mvvm.repository.source.remote

import android.content.Context
import com.google.gson.Gson
import com.template.mvvm.base.ext.android.content.res.read
import com.template.mvvm.repository.source.remote.feeds.licenses.LicensesData
import com.template.mvvm.repository.source.remote.feeds.products.ProductsData
import retrofit2.Call
import retrofit2.mock.BehaviorDelegate

internal class MockProductsApi(context: Context, private val delegate: BehaviorDelegate<ProductsApi>) : ProductsApi {
    private val ALL_PRODUCTS by lazy { context.assets.read("feeds/products/all.json") }
    private val MEN_PRODUCTS by lazy { context.assets.read("feeds/products/men.json") }
    private val WOMEN_PRODUCTS by lazy { context.assets.read("feeds/products/women.json") }
    private val EMPTY by lazy { context.assets.read("feeds/empty.json") }

    override fun getArticles(offset: Int): Call<ProductsData> {
        val feeds = if (offset > 0) EMPTY else ALL_PRODUCTS
        return delegate.returningResponse(Gson().fromJson(feeds, ProductsData::class.java))
                .getArticles(offset)
    }

    override fun filterArticles(offset: Int, keyword: String): Call<ProductsData> {
        val feeds = when (keyword) {
            "men" -> MEN_PRODUCTS
            "women" -> WOMEN_PRODUCTS
            else -> ALL_PRODUCTS
        }
        return delegate.returningResponse(Gson().fromJson(feeds, ProductsData::class.java))
                .filterArticles(offset, keyword)
    }
}

internal class MockLicensesApi(context: Context, private val delegate: BehaviorDelegate<LicensesApi>) : LicensesApi {
    private val ALL_LICENSES by lazy { context.assets.read("feeds/licenses/licenses-list.json") }

    override fun getLibraries(): Call<LicensesData> {
        val feeds = ALL_LICENSES
        return delegate.returningResponse(Gson().fromJson(feeds, LicensesData::class.java))
                .getLibraries()
    }
}
