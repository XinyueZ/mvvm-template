package com.template.mvvm.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.models.product.ProductItemViewModel
import com.template.mvvm.models.license.SoftwareLicenseItemViewModel

fun ProductList.setUpTransform(lifecycleOwner: LifecycleOwner, body: (t: List<ProductItemViewModel>?) -> Unit) {
    Transformations.switchMap(this) {
        val itemVmList = arrayListOf<ProductItemViewModel>().apply {
            it.mapTo(this) {
                ProductItemViewModel.from(it)
            }
        }
        SingleLiveData<List<ProductItemViewModel>>().apply {
            value = itemVmList
        }
    }.observe(lifecycleOwner, Observer { body(it) })
}

fun LibraryList.setUpTransform(lifecycleOwner: LifecycleOwner, body: (t: List<SoftwareLicenseItemViewModel>?) -> Unit) {
    Transformations.switchMap(this) {
        val itemVmList = arrayListOf<SoftwareLicenseItemViewModel>().apply {
            it.mapTo(this) {
                SoftwareLicenseItemViewModel.from(it)
            }
        }
        SingleLiveData<List<SoftwareLicenseItemViewModel>>().apply {
            value = itemVmList
        }
    }.observe(lifecycleOwner, Observer { body(it) })
}


