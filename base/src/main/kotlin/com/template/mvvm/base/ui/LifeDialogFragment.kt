package com.template.mvvm.base.ui

import android.app.Dialog
import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.View

abstract class LifeDialogFragment<out T : ViewModel> : AppCompatDialogFragment() {
    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun obtainViewModel(): T
    protected abstract fun onViewCreated(view: View): ViewDataBinding
    protected abstract fun requireViewModel(): Class<out T>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                it.layoutInflater.inflate(getLayout(), null).apply {
                    setView(this)
                    onViewCreated(this)
                }
            }.create()
        } ?: kotlin.run { super.onCreateDialog(savedInstanceState) }
    }
}