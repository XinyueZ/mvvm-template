package com.template.mvvm.base.ui

import android.arch.lifecycle.ViewModel

abstract class ViewModelFragment<VM : ViewModel> : LiveFragment() {
    protected fun requestViewModel(): Class<VM>? = arguments?.getSerializable("vm") as? Class<VM>
}

abstract class ViewModelDialogFragment<VM : ViewModel> : LiveDialogFragment() {
    protected fun requestViewModel(): Class<VM>? = arguments?.getSerializable("vm") as? Class<VM>
}