package com.template.mvvm.products

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.ProductsViewModel

class ProductsFragment : AppBaseFragment<ProductsViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, ProductsFragment::class.java.name) as ProductsFragment
    }

    private lateinit var binding: FragmentProductsBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductsBinding.bind(view)
                .apply {
                    vmItem = BR.vm
                    vm = obtainViewModel().apply {
                        registerLifecycleOwner(activity)
                        view.setupErrorSnackbar(this@ProductsFragment, this.onError)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = ProductsViewModel::class.java
}