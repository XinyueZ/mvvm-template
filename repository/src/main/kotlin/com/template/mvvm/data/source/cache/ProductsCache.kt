package com.template.mvvm.data.source.cache

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Single

class ProductsCache : ProductsDataSource {

    override fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}