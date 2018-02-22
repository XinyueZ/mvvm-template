package com.template.mvvm.app.product

import android.os.Bundle
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductsBinding
import com.template.mvvm.app.product.detail.ARG_SEL_ID
import com.template.mvvm.app.product.detail.ProductDetailActivity
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductsViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class ProductsFragment : AppBaseFragment<ProductsViewModel>() {

    override fun onViewCreated(view: View) = FragmentProductsBinding.bind(view).apply {
        vmItem = BR.vm
        vm = obtainViewModel().apply {
            registerLifecycleOwner(this@ProductsFragment)
            onError.setupErrorSnackbar(view, activity)
            openItemDetail.setupObserve(activity) {
                ProductDetailActivity::class.showSingleTopActivity(activity, Bundle().apply {
                    putLong(
                        ARG_SEL_ID, this@setupObserve
                    )
                })
            }
        }
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = ProductsViewModel::class.java
}