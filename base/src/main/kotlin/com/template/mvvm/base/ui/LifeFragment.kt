package com.template.mvvm.base.ui

import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class LifeFragment<out T : ViewModel> : Fragment() {
    protected abstract fun obtainViewModel(): T
    protected abstract @LayoutRes
    fun getLayout(): Int

    protected abstract fun bindingView(view: View): ViewDataBinding
    protected abstract fun requireViewModel(): Class<out T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = false
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayout(), container, false)
        return bindingView(view).apply { executePendingBindings() }.root
    }
}
