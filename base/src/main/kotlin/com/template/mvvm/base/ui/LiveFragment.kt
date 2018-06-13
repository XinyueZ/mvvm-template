package com.template.mvvm.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

abstract class LiveFragment : androidx.fragment.app.Fragment() {
    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun onViewCreated(view: View): ViewDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = false
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = onViewCreated(
        inflater.inflate(
            getLayout(),
            container,
            false
        )
    ).apply { executePendingBindings() }.root
}
