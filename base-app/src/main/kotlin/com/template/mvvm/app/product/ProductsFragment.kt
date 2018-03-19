package com.template.mvvm.app.product

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentProductsBinding
import com.template.mvvm.app.product.detail.ProductDetailActivity
import com.template.mvvm.base.ext.android.app.createActivityOptions
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ui.ViewModelFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.ProductsViewModel
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.arch.registerLifecycleOwner

class MenFragment : ProductsFragment<MenViewModel>()
class WomenFragment : ProductsFragment<WomenViewModel>()
class AllGenderFragment : ProductsFragment<AllGendersViewModel>()

sealed class ProductsFragment<VM : ProductsViewModel> : ViewModelFragment<VM>() {
    override fun onViewCreated(view: View) = FragmentProductsBinding.bind(view).apply {
        requestViewModel().get(activity) {
            vm = this
            registerLifecycleOwner(this@ProductsFragment)
            onError.setupErrorSnackbar(view, activity)
            controller.openItemDetail.setupObserve(activity) {
                ProductDetailActivity::class.showSingleTopActivity(
                    activity,
                    this.first,
                    activity.createActivityOptions(
                        this.second as? View,
                        getString(R.string.shared_object_name)
                    )
                )
            }
        }
    }

    override fun getLayout() = R.layout.fragment_products
}
