package com.template.mvvm.core.arch

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve

fun <E, VM : ViewModel, T : MutableLiveData<List<E>>> T.toViewModelList(
    lifecycleOwner: LifecycleOwner,
    mapFrom: (E) -> VM,
    body: (itemVmList: List<VM>?) -> Unit
): T {
    Transformations.switchMap(this) {
        SingleLiveData<List<VM>>().apply {
            value = arrayListOf<VM>().apply {
                it.mapTo(this) { mapFrom(it) }
            }
        }
    }.setupObserve(lifecycleOwner) { body(this) }
    return this
}