package com.template.mvvm.base.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.ViewDataBinding

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