package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentItem3Binding
import com.template.mvvm.models.AllBrandsViewModel

class Item3Fragment : AppBaseFragment<AllBrandsViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, Item3Fragment::class.java.name) as Item3Fragment
    }

    private lateinit var binding: FragmentItem3Binding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentItem3Binding.bind(view)
                .apply {
                    vm = obtainViewModel().apply { description.set(getString(R.string.navi_menu_item_3)) }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_item_3
    override fun requireViewModel() = AllBrandsViewModel::class.java
}