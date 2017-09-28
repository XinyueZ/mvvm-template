package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentMenBinding
import com.template.mvvm.models.MenViewModel

class MenFragment : AppBaseFragment<MenViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, MenFragment::class.java.name) as MenFragment
    }

    private lateinit var binding: FragmentMenBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentMenBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply { description.set(getString(R.string.action_men)) }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_men
    override fun requireViewModel() = MenViewModel::class.java
}