package com.template.mvvm.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.template.mvvm.ViewModelFactory
import com.template.mvvm.models.Error
import com.template.mvvm.models.ErrorViewModel

fun <T : ViewModel> obtainViewModel(activity: FragmentActivity, viewModelClass: Class<T>) =
        ViewModelProviders.of(activity, ViewModelFactory.getInstance(activity.application)).get(viewModelClass)

fun <T : ViewModel> obtainViewModel(fragment: Fragment, viewModelClass: Class<T>) =
        ViewModelProviders.of(fragment, ViewModelFactory.getInstance(fragment.activity.application)).get(viewModelClass)

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun View.showErrorSnackbar(errorVm: Error, timeLength: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, errorVm.wording, timeLength).setAction(errorVm.retryWording, { errorVm.retry() }).show()
}

fun View.setupErrorSnackbar(lifecycleOwner: LifecycleOwner,
                            liveData: ErrorViewModel, timeLength: Int = Snackbar.LENGTH_INDEFINITE) {
    liveData.observe(lifecycleOwner, Observer {
        it?.let { showErrorSnackbar(it, timeLength) }
    })
}



