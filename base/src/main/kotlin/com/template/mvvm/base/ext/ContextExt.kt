package com.template.mvvm.base.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.support.design.widget.Snackbar
import android.widget.Toast

fun Context.showToast(toastText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Toast.makeText(this, toastText, timeLength).show()
}

fun Context.setupToast(
    lifecycleOwner: LifecycleOwner,
    liveData: LiveData<String>, timeLength: Int = Toast.LENGTH_SHORT
) {
    liveData.observe(lifecycleOwner, Observer {
        it?.let { showToast(it, timeLength) }
    })
}