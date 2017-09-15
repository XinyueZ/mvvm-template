package com.template.mvvm.data.domain.licenses

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.content.Context
import com.template.mvvm.licenses.list.SoftwareLicenseItemViewModel
import com.template.mvvm.life.SingleLiveData

class LibraryList : SingleLiveData<List<Library>>() {
    inline fun switchMapViewModelList(lifecycleRegistryOwner: LifecycleRegistryOwner, crossinline body: (t: List<SoftwareLicenseItemViewModel>?) -> Unit) {
        when (lifecycleRegistryOwner is Context) {
            true -> {
                with((lifecycleRegistryOwner as Context).applicationContext as Application) {
                    Transformations.switchMap(this@LibraryList) {
                        val itemVmList = arrayListOf<SoftwareLicenseItemViewModel>().apply {
                            it.mapTo(this) {
                                SoftwareLicenseItemViewModel.from(this@with, it)
                            }
                        }
                        SingleLiveData<List<SoftwareLicenseItemViewModel>>().apply {
                            value = itemVmList
                            return@switchMap this
                        }
                    }.observe(lifecycleRegistryOwner, Observer { body(it) })
                }
            }
        }
    }
}
