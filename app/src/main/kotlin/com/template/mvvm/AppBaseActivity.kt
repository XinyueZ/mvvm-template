package com.template.mvvm

import android.arch.lifecycle.AndroidViewModel
import com.template.mvvm.ui.LifeActivity
import com.template.mvvm.vm.obtainViewModel

abstract class AppBaseActivity<out T : AndroidViewModel> : LifeActivity<T>() {
    override fun obtainViewModel() = obtainViewModel(createViewModel())
}