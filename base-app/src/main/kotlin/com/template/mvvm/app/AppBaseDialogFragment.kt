package com.template.mvvm.app

import android.arch.lifecycle.ViewModel
import com.template.mvvm.base.ui.LifeDialogFragment
import com.template.mvvm.core.ext.obtainViewModel

abstract class AppBaseDialogFragment<out T : ViewModel> : LifeDialogFragment<T>() {
    override fun obtainViewModel() = obtainViewModel(requireViewModel())
}