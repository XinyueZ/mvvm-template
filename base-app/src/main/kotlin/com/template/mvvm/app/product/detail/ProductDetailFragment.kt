package com.template.mvvm.app.product.detail

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductDetailBinding
import com.template.mvvm.base.ext.putObserver
import com.template.mvvm.base.ext.setUpActionBar
import com.template.mvvm.core.ext.setupErrorSnackbar
import com.template.mvvm.core.models.product.ProductDetailViewModel

class ProductDetailFragment : AppBaseFragment<ProductDetailViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, ProductDetailFragment::class.java.name) as ProductDetailFragment
    }

    private lateinit var binding: FragmentProductDetailBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductDetailBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        activity?.let {
                            productIdToDetail = it.intent.extras[ProductDetailActivity.ARG_SEL_ID] as Long?
                            registerLifecycle(it)
                        }
                        view.setupErrorSnackbar(this@ProductDetailFragment, this.onError)
                        lifecycle.putObserver(this)
                    }
                    activity.setUpActionBar(toolbar) {
                        setDisplayHomeAsUpEnabled(true)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_product_detail
    override fun requireViewModel() = ProductDetailViewModel::class.java
}