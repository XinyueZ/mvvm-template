package com.template.mvvm.app

import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding
import com.template.mvvm.base.ui.LifeActivity
import com.template.mvvm.core.obtainViewModel

abstract class AppBaseActivity<out T : ViewModel, in B : ViewDataBinding> : LifeActivity<T, B>() {
    override fun obtainViewModel() = obtainViewModel(requireViewModel())
}