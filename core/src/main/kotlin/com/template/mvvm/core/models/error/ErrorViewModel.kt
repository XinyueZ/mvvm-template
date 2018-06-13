package com.template.mvvm.core.models.error

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.template.mvvm.core.arch.AbstractViewModel

class ErrorViewModel : MutableLiveData<Error>() {
    val t: Throwable? = value?.t
    @StringRes
    val wording: Int? = value?.wording
    @StringRes
    val retryWording: Int? = value?.retryWording
    val retry: (() -> Unit)? = value?.retry
}

class Error(
    val t: Throwable, @StringRes val wording: Int, @StringRes val retryWording: Int,
    val retry: () -> Unit
) : AbstractViewModel()

fun View.showErrorSnackbar(errorVm: Error, timeLength: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, errorVm.wording, timeLength)
        .setAction(errorVm.retryWording, { errorVm.retry() }).show()
}

fun View.setupErrorSnackbar(
    lifecycleOwner: LifecycleOwner,
    liveData: ErrorViewModel, timeLength: Int = Snackbar.LENGTH_INDEFINITE
) {
    liveData.observe(lifecycleOwner, Observer {
        it?.let { showErrorSnackbar(it, timeLength) }
    })
}

fun ErrorViewModel?.setupErrorSnackbar(
    v: View,
    lifecycleOwner: LifecycleOwner?,
    timeLength: Int = Snackbar.LENGTH_INDEFINITE
) {
    this?.apply {
        lifecycleOwner?.apply {
            v.setupErrorSnackbar(this, this@setupErrorSnackbar, timeLength)
        }
    }
}