package com.template.mvvm.core.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import com.template.mvvm.core.ViewModelFactory
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(this.application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    activity?.let {
        ViewModelProviders.of(it, ViewModelFactory.getInstance(it.application)).get(viewModelClass)
    }
            ?: kotlin.run { ViewModelProviders.of(this).get(viewModelClass) }

fun <T : ViewModel> LifecycleOwner.obtainViewModel(viewModelClass: Class<T>) = with(
    when (this) {
        is Fragment -> activity
        else -> this as FragmentActivity
    }
) {
    this?.let {
        ViewModelProviders.of(it, ViewModelFactory.getInstance(it.application)).get(viewModelClass)
    }
            ?: kotlin.run { throw IllegalStateException("LifecycleOwner is not a type of fragment or activity.") }
}

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




