package com.template.mvvm.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.WomenViewModel
import com.template.mvvm.products.detail.ProductDetailActivity

class WomenFragment : AppBaseFragment<WomenViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, WomenFragment::class.java.name) as WomenFragment
    }

    private lateinit var binding: FragmentProductsBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentProductsBinding.bind(view)
                .apply {
                    vmItem = BR.vm
                    vm = obtainViewModel().apply {
                        activity?.let {
                            with(it) {
                                registerLifecycleOwner(it)
                                view.setupErrorSnackbar(it, onError)

                                openProductDetail.observe(it, Observer {
                                    it?.let {
                                        ProductDetailActivity.showInstance(this@with, it)
                                    }
                                })
                            }
                        }
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = WomenViewModel::class.java
}