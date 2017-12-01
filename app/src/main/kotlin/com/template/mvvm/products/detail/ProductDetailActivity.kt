package com.template.mvvm.products.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivityProductDetailBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.ProductDetailViewModel

class ProductDetailActivity : AppBaseActivity<ProductDetailViewModel>() {
    companion object {
        internal val ARG_SEL_ID = "detail-item-id"
        fun showInstance(cxt: Activity, selectedId: Long) {
            val intent = Intent(cxt, ProductDetailActivity::class.java)
            intent.putExtra(ARG_SEL_ID, selectedId)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override @LayoutRes
    fun getLayout() = R.layout.activity_product_detail

    override fun requireViewModel() = ProductDetailViewModel::class.java
    override fun createViewModelView() = ProductDetailFragment.newInstance(application)

    lateinit var binding: ActivityProductDetailBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityProductDetailBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            contentFrame.apply {
                vm = obtainViewModel().apply {
                    showSystemUi.observe(this@ProductDetailActivity, Observer {
                        when (it) {
                            true -> hideSystemUi(1500)
                            false -> showSystemUi()
                        }
                    })
                    setupErrorSnackbar(this@ProductDetailActivity, this.onError)
                }
            }
        }
    }
}