package com.template.mvvm.app.product

import android.view.View
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductsBinding
import com.template.mvvm.app.product.detail.ProductDetailActivity
import com.template.mvvm.base.ext.android.app.createActivityOptions
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ui.LiveFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductsViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class ProductsFragment : LiveFragment() {

    override fun onViewCreated(view: View) = FragmentProductsBinding.bind(view).apply {
        vmItem = BR.vm
        ProductsViewModel::class.get(this@ProductsFragment) {
            vm = this
            registerLifecycleOwner(this@ProductsFragment)
            onError.setupErrorSnackbar(view, activity)
            controller.openItemDetail.setupObserve(activity) {
                ProductDetailActivity::class.showSingleTopActivity(
                    activity,
                    this.first,
                    activity.createActivityOptions(
                        this.second as? View,
                        getString(R.string.shared_object_name)
                    )
                )
            }
        }
    }

    override fun getLayout() = R.layout.fragment_products
}