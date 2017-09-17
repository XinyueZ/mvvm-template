package com.template.mvvm.data.domain.licenses

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.content.Context
import com.template.mvvm.licenses.list.SoftwareLicenseItemViewModel
import com.template.mvvm.life.SingleLiveData

class LibraryList : SingleLiveData<List<Library>>() {
    inline fun switchMapViewModelList(lifecycleOwner: LifecycleOwner, crossinline body: (t: List<SoftwareLicenseItemViewModel>?) -> Unit) {
        when (lifecycleOwner is Context) {
            true -> {
                with((lifecycleOwner as Context).applicationContext as Application) {
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
                    }.observe(lifecycleOwner, Observer { body(it) })
                }
            }
        }
    }
}
