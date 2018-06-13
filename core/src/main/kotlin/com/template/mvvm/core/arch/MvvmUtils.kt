package com.template.mvvm.core.arch

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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