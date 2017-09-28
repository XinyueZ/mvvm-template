package com.template.mvvm.ui

import android.app.Dialog
import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.View

abstract class LifeDialogFragment<out T : ViewModel> : AppCompatDialogFragment() {
    protected abstract fun obtainViewModel(): T
    protected abstract @LayoutRes fun getLayout(): Int
    protected abstract fun bindingView(view: View): ViewDataBinding
    protected abstract fun requireViewModel(): Class<out T>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).apply {
            activity.layoutInflater.inflate(getLayout(), null).apply {
                setView(this)
                bindingView(this)
            }
        }.create()
    }
}