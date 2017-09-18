package com.template.mvvm.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity

fun <T : ViewModel> ViewModel.obtainViewModel(activity: FragmentActivity, viewModelClass: Class<T>) =
        ViewModelProviders.of(activity, ViewModelFactory.getInstance(activity.application)).get(viewModelClass)

fun <T : ViewModel> ViewModel.obtainViewModel(fragment: Fragment, viewModelClass: Class<T>) =
        ViewModelProviders.of(fragment, ViewModelFactory.getInstance(fragment.activity.application)).get(viewModelClass)


fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)


