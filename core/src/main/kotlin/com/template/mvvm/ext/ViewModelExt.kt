package com.template.mvvm.ext

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.template.mvvm.vm.ViewModelFactory

fun <T : ViewModel> ViewModel.obtainViewModel(activity: FragmentActivity, viewModelClass: Class<T>) =
        ViewModelProviders.of(activity, ViewModelFactory.getInstance(activity.application)).get(viewModelClass)

fun <T : ViewModel> ViewModel.obtainViewModel(fragment: Fragment, viewModelClass: Class<T>) =
        ViewModelProviders.of(fragment, ViewModelFactory.getInstance(fragment.activity.application)).get(viewModelClass)


