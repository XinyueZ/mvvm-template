package com.template.mvvm.products

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivityProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.product.ProductsViewModel

class ProductsActivity : AppBaseActivity<ProductsViewModel>() {
    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, ProductsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override @LayoutRes
    fun getLayout() = R.layout.activity_products

    override fun requireViewModel() = ProductsViewModel::class.java
    override fun createViewModelView() = ProductsFragment.newInstance(application)

    lateinit var binding: ActivityProductsBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityProductsBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            contentFrame.apply {
                vm = obtainViewModel().apply {
                    showSystemUi.observe(this@ProductsActivity, Observer {
                        when (it) {
                            true -> hideSystemUi(1500)
                            false -> showSystemUi()
                        }
                    })
                    setupErrorSnackbar(this@ProductsActivity, this.onError)
                }
            }
        }
    }
}