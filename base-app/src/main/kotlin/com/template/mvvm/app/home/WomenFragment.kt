package com.template.mvvm.app.home

import android.os.Bundle
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductsBinding
import com.template.mvvm.app.product.detail.ProductDetailActivity
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class WomenFragment : AppBaseFragment<WomenViewModel>() {

    override fun bindingView(view: View) = FragmentProductsBinding.bind(view).apply {
        vmItem = BR.vm
        vm = obtainViewModel().apply {
            registerLifecycleOwner(this@WomenFragment)
            onError.setupErrorSnackbar(view, activity)
            openItemDetail.setupObserve(activity) {
                ProductDetailActivity::class.showSingleTopActivity(activity, Bundle().apply {
                    putLong(
                        ProductDetailActivity.ARG_SEL_ID, this@setupObserve
                    )
                })
            }
        }
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = WomenViewModel::class.java
}