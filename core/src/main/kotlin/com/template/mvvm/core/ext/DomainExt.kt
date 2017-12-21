package com.template.mvvm.core.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.core.models.license.SoftwareLicenseItemViewModel
import com.template.mvvm.core.models.product.ProductItemViewModel
import com.template.mvvm.repository.domain.licenses.LibraryList
import com.template.mvvm.repository.domain.products.ProductList

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


