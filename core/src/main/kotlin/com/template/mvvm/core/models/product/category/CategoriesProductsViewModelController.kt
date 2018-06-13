package com.template.mvvm.core.models.product.category

import android.os.Bundle
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.repository.domain.products.ProductCategoryList

class CategoriesProductsViewModelController : BaseObservable() {
    var productCategoryListSource: ProductCategoryList? = null
    var productCategoryItemVmList = SingleLiveData<List<ViewModel>>()
    //Detail to open
    val openItemDetail: MutableLiveData<Pair<Bundle, Any?>> =
        SingleLiveData()
    val showSystemUi: MutableLiveData<Boolean> =
        SingleLiveData()
}