package com.template.mvvm.app.product.detail

import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductDetailBinding
import com.template.mvvm.app.product.detail.ProductDetailActivity.Companion.ARG_SEL_ID
import com.template.mvvm.base.ext.android.app.getExtras
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.app.setViewGoldenRatioHeight
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductDetailViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class ProductDetailFragment : AppBaseFragment<ProductDetailViewModel>() {
    override fun bindingView(view: View) = FragmentProductDetailBinding.bind(view).apply {
        vm = obtainViewModel().apply {
            productIdToDetail = activity.getExtras(ARG_SEL_ID)
            onError.setupErrorSnackbar(view, activity)
            registerLifecycleOwner(this@ProductDetailFragment)
            activity.setUpActionBar(toolbar) {
                setDisplayHomeAsUpEnabled(true)
            }
            activity.setViewGoldenRatioHeight(appbar)
        }
    }

    override fun getLayout() = R.layout.fragment_product_detail
    override fun requireViewModel() = ProductDetailViewModel::class.java
}