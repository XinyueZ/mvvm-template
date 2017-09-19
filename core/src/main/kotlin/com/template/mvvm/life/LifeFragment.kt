package com.template.mvvm.life

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class LifeFragment : Fragment() {
    protected fun obtainViewModel() = (activity as LifeActivity).obtainViewModel()
    protected abstract fun getLayout(): Int
    protected abstract fun bindingView(view: View): ViewDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = false
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayout(), container, false)
        return bindingView(view).root
    }
}
