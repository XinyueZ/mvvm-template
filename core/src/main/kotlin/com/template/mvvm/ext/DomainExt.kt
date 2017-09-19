package com.template.mvvm.ext

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.content.Context
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.models.ProductItemViewModel
import com.template.mvvm.models.SoftwareLicenseItemViewModel

fun ProductList.switchMapViewModelList(lifecycleOwner: LifecycleOwner, body: (t: List<ProductItemViewModel>?) -> Unit) {
    when (lifecycleOwner is Context) {
        true -> {
            val cxt = (lifecycleOwner as Context).applicationContext
            when (cxt) {
                is Application -> {
                    Transformations.switchMap(this) {
                        val itemVmList = arrayListOf<ProductItemViewModel>().apply {
                            it.mapTo(this) {
                                ProductItemViewModel.from(cxt, it)
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

fun LibraryList.switchMapViewModelList(lifecycleOwner: LifecycleOwner, body: (t: List<SoftwareLicenseItemViewModel>?) -> Unit) {
    when (lifecycleOwner is Context) {
        true -> {
            val cxt = (lifecycleOwner as Context).applicationContext
            when (cxt) {
                is Application -> {
                    Transformations.switchMap(this) {
                        val itemVmList = arrayListOf<SoftwareLicenseItemViewModel>().apply {
                            it.mapTo(this) {
                                SoftwareLicenseItemViewModel.from(cxt, it)
                            }
                        }
                        SingleLiveData<List<SoftwareLicenseItemViewModel>>().apply {
                            value = itemVmList
                            return@switchMap this
                        }
                    }.observe(lifecycleOwner, Observer { body(it) })
                }
            }
        }
    }
}
