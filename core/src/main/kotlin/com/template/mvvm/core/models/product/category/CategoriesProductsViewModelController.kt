package com.template.mvvm.core.models.product.category

import android.arch.lifecycle.ViewModel
import android.databinding.BaseObservable
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.repository.domain.products.ProductCategoryList

class CategoriesProductsViewModelController : BaseObservable() {
    var productCategoryListSource: ProductCategoryList? = null
    var productCategoryItemVmList = SingleLiveData<List<ViewModel>>()

}