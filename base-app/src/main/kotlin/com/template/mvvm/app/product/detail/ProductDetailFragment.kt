package com.template.mvvm.app.product.detail

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductDetailBinding
import com.template.mvvm.base.ext.android.app.getExtras
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.app.setViewGoldenRatioHeight
import com.template.mvvm.base.ui.LiveFragment
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductDetailViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class ProductDetailFragment : LiveFragment<ProductDetailViewModel>() {

    override fun onViewCreated(view: View) = FragmentProductDetailBinding.bind(view).apply {
        ProductDetailViewModel::class.generateViewModel(this@ProductDetailFragment) {
            vm = this
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
}