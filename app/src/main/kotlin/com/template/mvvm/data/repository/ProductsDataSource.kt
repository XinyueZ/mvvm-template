package com.template.mvvm.data.repository

import android.arch.lifecycle.LifecycleRegistryOwner
import com.template.mvvm.data.domain.products.ProductList
import io.reactivex.Single

interface ProductsDataSource {
    fun getAllProducts(lifecycleOwner: LifecycleRegistryOwner): Single<ProductList>
}