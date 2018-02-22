package com.template.mvvm.app.product

import android.support.annotation.LayoutRes
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityProductsBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.base.ui.LiveActivity
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductsViewModel

class ProductsActivity : LiveActivity<ProductsViewModel, ActivityProductsBinding>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_products

    override fun createViewModelView() = ProductsFragment::class.newInstance(application)
    override fun onCreate(binding: ActivityProductsBinding) {
        setUpActionBar(binding.toolbar)
        ProductsViewModel::class.generateViewModel(this) {
            binding.vm = this
            showSystemUi.setupObserve(this@ProductsActivity) {
                execute({ hideSystemUi(1500) }, { showSystemUi() })
            }
            onError.setupErrorSnackbar(binding.contentFrame, this@ProductsActivity)
        }
    }
}