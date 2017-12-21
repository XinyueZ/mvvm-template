package com.template.mvvm.app

import android.arch.lifecycle.ViewModel
import com.template.mvvm.base.ui.LifeFragment
import com.template.mvvm.core.ext.obtainViewModel

abstract class AppBaseFragment<out T : ViewModel>: LifeFragment<T>() {
    override fun obtainViewModel() = obtainViewModel(requireViewModel())
}