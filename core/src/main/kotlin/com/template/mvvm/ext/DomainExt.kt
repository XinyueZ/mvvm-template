package com.template.mvvm.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.domain.products.BrandList
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.models.BrandItemViewModel
import com.template.mvvm.models.ProductItemViewModel
import com.template.mvvm.models.SoftwareLicenseItemViewModel

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

fun BrandList.setUpTransform(lifecycleOwner: LifecycleOwner, body: (t: List<BrandItemViewModel>?) -> Unit) {
    Transformations.switchMap(this) {
        val itemVmList = arrayListOf<BrandItemViewModel>().apply {
            it.mapTo(this) {
                BrandItemViewModel.from(it)
            }
        }
        SingleLiveData<List<BrandItemViewModel>>().apply {
            value = itemVmList
        }
    }.observe(lifecycleOwner, Observer { body(it) })
}
