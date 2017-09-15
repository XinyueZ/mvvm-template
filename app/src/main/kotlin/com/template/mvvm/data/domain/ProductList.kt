package com.template.mvvm.data.domain

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations.switchMap
import android.content.Context
import com.template.mvvm.life.SingleLiveData
import com.template.mvvm.products.list.ProductItemViewModel

class ProductList : SingleLiveData<List<Product>>() {
    inline fun switchMapViewModelList(lifecycleRegistryOwner: LifecycleRegistryOwner, crossinline body: (t: List<ProductItemViewModel>?) -> Unit) {
        when (lifecycleRegistryOwner is Context) {
            true -> {
                with((lifecycleRegistryOwner as Context).applicationContext as Application) {
                    switchMap(this@ProductList) {
                        val itemVmList = arrayListOf<ProductItemViewModel>().apply {
                            it.mapTo(this) {
                                ProductItemViewModel.from(this@with, it)
                            }
                        }
                        SingleLiveData<List<ProductItemViewModel>>().apply {
                            value = itemVmList
                            return@switchMap this
                        }
                    }.observe(lifecycleRegistryOwner, Observer { body(it) })
                }
            }
        }
    }
}