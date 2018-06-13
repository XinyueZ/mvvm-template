package com.template.mvvm.core.models.home

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableInt
import com.template.mvvm.core.R

class HomeViewModelState : BaseObservable() {
    val title = ObservableInt(R.string.home_title)
    val selectItem = ObservableInt()
}