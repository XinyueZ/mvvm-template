package com.template.mvvm.models.home

import android.databinding.BaseObservable
import android.databinding.ObservableInt
import com.template.mvvm.R

class HomeViewModelState : BaseObservable() {

    val title = ObservableInt(R.string.home_title)
}