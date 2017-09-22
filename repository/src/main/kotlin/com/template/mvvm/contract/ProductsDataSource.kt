package com.template.mvvm.contract

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Single

interface ProductsDataSource : DataSource {
    fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList>
}