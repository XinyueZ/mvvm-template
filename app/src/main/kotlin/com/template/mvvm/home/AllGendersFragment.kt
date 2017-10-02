package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.AllGendersViewModel

class AllGendersFragment : AppBaseFragment<AllGendersViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, AllGendersFragment::class.java.name) as AllGendersFragment
    }

    private lateinit var binding: FragmentProductsBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductsBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        activity.apply {
                            registerLifecycleOwner(this)
                            view.setupErrorSnackbar(this, onError)
                        }
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = AllGendersViewModel::class.java
}