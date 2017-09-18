package com.template.mvvm.vm.domain.products

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations.switchMap
import android.content.Context
import com.template.mvvm.life.SingleLiveData
import com.template.mvvm.vm.models.ProductItemViewModel

class ProductList : SingleLiveData<List<Product>>() {
    inline fun switchMapViewModelList(lifecycleOwner: LifecycleOwner, crossinline body: (t: List<ProductItemViewModel>?) -> Unit) {
        when (lifecycleOwner is Context) {
            true -> {
                with((lifecycleOwner as Context).applicationContext as Application) {
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
                    }.observe(lifecycleOwner, Observer { body(it) })
                }
            }
        }
    }
}