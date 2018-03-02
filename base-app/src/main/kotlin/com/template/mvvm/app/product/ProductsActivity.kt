package com.template.mvvm.app.product

import android.support.annotation.LayoutRes
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityProductsBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.base.ui.LiveActivity
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.AllGendersViewModel

class ProductsActivity : LiveActivity<ActivityProductsBinding>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_products

    override fun createLiveFragment() = ProductsFragment::class.newInstance(application)
    override fun onCreate(binding: ActivityProductsBinding) {
        setUpActionBar(binding.toolbar)
        AllGendersViewModel::class.get(this) {
            binding.vm = this
            controller.showSystemUi.setupObserve(this@ProductsActivity) {
                execute({ hideSystemUi(1500) }, { showSystemUi() })
            }
            onError.setupErrorSnackbar(binding.contentFrame, this@ProductsActivity)
        }
    }
}