package com.template.mvvm.app.product

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityProductsBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductsViewModel

class ProductsActivity : AppBaseActivity<ProductsViewModel>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_products

    override fun requireViewModel() = ProductsViewModel::class.java
    override fun createViewModelView() = ProductsFragment::class.newInstance(application)

    lateinit var binding: ActivityProductsBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityProductsBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        binding.vm = obtainViewModel().apply {
            showSystemUi.setupObserve(this@ProductsActivity) {
                execute({ hideSystemUi(1500) }, { showSystemUi() })
            }
            onError.setupErrorSnackbar(binding.contentFrame, this@ProductsActivity)
        }
    }
}