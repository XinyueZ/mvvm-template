package com.template.mvvm

import android.arch.lifecycle.ViewModel
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ui.LifeActivity

abstract class AppBaseActivity<out T : ViewModel> : LifeActivity<T>() {
    override fun obtainViewModel() = obtainViewModel(requireViewModel())
}