package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentItem3Binding
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.models.AppNavigationViewModel
import com.template.mvvm.models.HomeViewModel

class Item3Fragment : AppBaseFragment<HomeViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, Item3Fragment::class.java.name) as Item3Fragment
    }

    private lateinit var binding: FragmentItem3Binding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentItem3Binding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        (activity as HomeActivity).binding.vm =
                                this@Item3Fragment.obtainViewModel(AppNavigationViewModel::class.java)
                        description.set(getString(R.string.navi_menu_item_3))
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_item_3
    override fun requireViewModel() = HomeViewModel::class.java
}