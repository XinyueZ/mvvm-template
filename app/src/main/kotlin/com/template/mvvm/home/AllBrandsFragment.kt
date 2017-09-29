package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentAllBrandsBinding
import com.template.mvvm.ext.getDimensionPixel
import com.template.mvvm.ext.getScreenSize
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.AllBrandsViewModel

class AllBrandsFragment : AppBaseFragment<AllBrandsViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, AllBrandsFragment::class.java.name) as AllBrandsFragment
    }

    private lateinit var binding: FragmentAllBrandsBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentAllBrandsBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        itemWidth = (getScreenSize().width - getDimensionPixel(R.dimen.general_padding) * 2) / 2
                        itemHeight = itemWidth
                        registerLifecycleOwner(activity)
                        view.setupErrorSnackbar(activity, this.onError)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_all_brands
    override fun requireViewModel() = AllBrandsViewModel::class.java
}