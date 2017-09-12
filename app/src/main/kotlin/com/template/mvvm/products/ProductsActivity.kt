package com.template.mvvm.products

import android.app.Activity
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivityProductsBinding
import com.template.mvvm.life.LifeActivity

class ProductsActivity : LifeActivity() {
    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, ProductsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override fun getLayout() = R.layout.activity_products
    override fun createViewModel() = ProductsViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            ProductsFragment.newInstance(application)) as LifecycleFragment

    lateinit var binding: ActivityProductsBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityProductsBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = obtainViewModel() as ProductsViewModel
        vm.pageStill.observe(this, Observer { hideSystemUi(1500) })
    }
}