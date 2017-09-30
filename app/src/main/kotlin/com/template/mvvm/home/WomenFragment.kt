package com.template.mvvm.home

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.WomenViewModel
import com.template.mvvm.products.ProductsFragment

class WomenFragment : AppBaseFragment<WomenViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, ProductsFragment::class.java.name) as ProductsFragment
    }

    private lateinit var binding: FragmentProductsBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductsBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        registerLifecycleOwner(activity)
                        view.setupErrorSnackbar(this@WomenFragment, this.onError)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = WomenViewModel::class.java
}