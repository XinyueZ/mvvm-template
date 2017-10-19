package com.template.mvvm.products.detail

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductDetailBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.ProductDetailViewModel

class ProductDetailFragment : AppBaseFragment<ProductDetailViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, ProductDetailFragment::class.java.name) as ProductDetailFragment
    }

    private lateinit var binding: FragmentProductDetailBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductDetailBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        productIdToDetail = activity.intent.extras[ProductDetailActivity.ARG_SEL_ID] as String?
                        registerLifecycleOwner(activity)
                        view.setupErrorSnackbar(this@ProductDetailFragment, this.onError)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_product_detail
    override fun requireViewModel() = ProductDetailViewModel::class.java
}