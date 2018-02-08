package com.template.mvvm.app.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductsBinding
import com.template.mvvm.app.product.detail.ProductDetailActivity
import com.template.mvvm.core.ext.setupErrorSnackbar
import com.template.mvvm.core.models.product.AllGendersViewModel

class AllGendersFragment : AppBaseFragment<AllGendersViewModel>() {

    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, AllGendersFragment::class.java.name) as AllGendersFragment
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

                                openItemDetail.observe(it, Observer {
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
    override fun requireViewModel() = AllGendersViewModel::class.java
    override fun onStop() {
        obtainViewModel().reset()
        super.onStop()
    }
}