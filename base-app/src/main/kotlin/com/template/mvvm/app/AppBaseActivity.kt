package com.template.mvvm.app

import android.arch.lifecycle.ViewModel
import com.template.mvvm.base.ui.LifeActivity
import com.template.mvvm.core.obtainViewModel

abstract class AppBaseActivity<out T : ViewModel> : LifeActivity<T>() {
    override fun obtainViewModel() = obtainViewModel(requireViewModel())
}