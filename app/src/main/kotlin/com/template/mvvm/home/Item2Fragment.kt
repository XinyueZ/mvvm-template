package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentItem2Binding
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.models.AppNavigationViewModel
import com.template.mvvm.models.HomeViewModel

class Item2Fragment : AppBaseFragment<HomeViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, Item2Fragment::class.java.name) as Item2Fragment
    }

    private lateinit var binding: FragmentItem2Binding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentItem2Binding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        (activity as HomeActivity).binding.vm =
                                this@Item2Fragment.obtainViewModel(AppNavigationViewModel::class.java)
                        description.set(getString(R.string.navi_menu_item_2))
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_item_2
    override fun requireViewModel() = HomeViewModel::class.java
}