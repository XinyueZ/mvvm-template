package com.template.mvvm.app.product.detail

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductDetailBinding
import com.template.mvvm.base.ext.android.app.getExtras
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.app.setViewGoldenRatioHeight
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.android.widget.setPalette
import com.template.mvvm.base.ui.LiveFragment
import com.template.mvvm.core.ARG_SEL_ID
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.detail.ProductDetailViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class ProductDetailFragment : LiveFragment() {

    override fun onViewCreated(view: View) = FragmentProductDetailBinding.bind(view).apply {
        ProductDetailViewModel::class.get(this@ProductDetailFragment) {
            vm = this
            productIdToDetail = activity.getExtras(ARG_SEL_ID)
            onError.setupErrorSnackbar(view, activity)
            registerLifecycleOwner(this@ProductDetailFragment)
            activity.setViewGoldenRatioHeight(appbar)?.also {
                it.setUpActionBar(toolbar) {
                    setDisplayHomeAsUpEnabled(true)
                }
            }
            controller.palette.setupObserve(this@ProductDetailFragment) {
                toolbar.setPalette(this)
                collapsingToolbar.setPalette(this)
            }
        }
    }

    override fun getLayout() = R.layout.fragment_product_detail
}