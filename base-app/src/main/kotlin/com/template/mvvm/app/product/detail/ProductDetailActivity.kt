package com.template.mvvm.app.product.detail

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityProductDetailBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductDetailViewModel

class ProductDetailActivity : AppBaseActivity<ProductDetailViewModel>() {
    companion object {
        internal val ARG_SEL_ID = "detail-item-id"
    }

    @LayoutRes
    override fun getLayout() = R.layout.activity_product_detail

    override fun requireViewModel() = ProductDetailViewModel::class.java
    override fun createViewModelView() = ProductDetailFragment::class.newInstance(application)

    lateinit var binding: ActivityProductDetailBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityProductDetailBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = obtainViewModel().apply {
            showSystemUi.setupObserve(this@ProductDetailActivity) {
                execute({ hideSystemUi(1500) }, { showSystemUi() })
            }
            onError.setupErrorSnackbar(binding.contentFrame, this@ProductDetailActivity)
        }
    }
}