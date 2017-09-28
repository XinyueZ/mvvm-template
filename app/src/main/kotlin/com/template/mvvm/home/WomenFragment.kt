package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentWomenBinding
import com.template.mvvm.models.WomenViewModel

class WomenFragment : AppBaseFragment<WomenViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, WomenFragment::class.java.name) as WomenFragment
    }

    private lateinit var binding: FragmentWomenBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentWomenBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply { description.set(getString(R.string.action_women)) }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_women
    override fun requireViewModel() = WomenViewModel::class.java
}