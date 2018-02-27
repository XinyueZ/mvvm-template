package com.template.mvvm.core.models.home

import android.databinding.BaseObservable
import android.databinding.ObservableInt
import com.template.mvvm.core.R

class HomeViewModelState : BaseObservable() {
    val title = ObservableInt(R.string.home_title)
}