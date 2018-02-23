package com.template.mvvm.base.ui

import android.app.Dialog
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.View

abstract class LiveDialogFragment : AppCompatDialogFragment() {
    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun onViewCreated(view: View): ViewDataBinding

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