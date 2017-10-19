package com.template.mvvm.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentProductsBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.AllGendersViewModel
import com.template.mvvm.products.detail.ProductDetailActivity

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
                        activity.apply {
                            registerLifecycleOwner(activity)
                            view.setupErrorSnackbar(this, onError)

                            openProductDetail.observe(activity, Observer {
                                it?.let {
                                    ProductDetailActivity.showInstance(activity, it)
                                }
                            })
                        }
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_products
    override fun requireViewModel() = AllGendersViewModel::class.java
}