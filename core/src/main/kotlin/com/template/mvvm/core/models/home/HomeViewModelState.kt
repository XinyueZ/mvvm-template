package com.template.mvvm.core.models.home

import android.databinding.BaseObservable
import android.databinding.ObservableInt
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.SingleLiveData

class HomeViewModelState : BaseObservable() {
    val defaultSelection = SingleLiveData<Int>()
    val title = ObservableInt(R.string.home_title)
}