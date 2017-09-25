package com.template.mvvm.products

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.ProductItemViewModel
import com.template.mvvm.models.ProductsViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ProductsFragment : AppBaseFragment<ProductsViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, ProductsFragment::class.java.name) as ProductsFragment
    }

    private lateinit var binding: FragmentProductsBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductsBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        itemBinding = ItemBinding.of<ProductItemViewModel>(BR.vm, R.layout.item_product)
                        registerLifecycleOwner(activity as LifecycleOwner)
                        view.setupErrorSnackbar(this@ProductsFragment, this.onError)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_products
}