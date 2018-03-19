package com.template.mvvm.app.product.category

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentCategoriesProductsBinding
import com.template.mvvm.app.product.detail.ProductDetailActivity
import com.template.mvvm.base.ext.android.app.createActivityOptions
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ui.ViewModelFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.product.category.CategoriesProductsViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class CategoriesProductsFragment : AbstractCategoriesProductsFragment<CategoriesProductsViewModel>()

sealed class AbstractCategoriesProductsFragment<VM : CategoriesProductsViewModel> : ViewModelFragment<VM>() {
    override fun onViewCreated(view: View) = FragmentCategoriesProductsBinding.bind(view).apply {
        requestViewModel().get(activity) {
            vm = this
            registerLifecycleOwner(this@AbstractCategoriesProductsFragment)
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

    override fun getLayout() = R.layout.fragment_categories_products
}
