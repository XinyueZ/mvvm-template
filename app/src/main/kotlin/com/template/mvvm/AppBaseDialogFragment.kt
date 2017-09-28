package com.template.mvvm

import android.arch.lifecycle.ViewModel
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ui.LifeDialogFragment

abstract class AppBaseDialogFragment<out T : ViewModel> : LifeDialogFragment<T>() {
    override fun obtainViewModel() = obtainViewModel(requireViewModel())
}