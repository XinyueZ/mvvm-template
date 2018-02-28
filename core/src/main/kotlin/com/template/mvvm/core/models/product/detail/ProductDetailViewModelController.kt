package com.template.mvvm.core.models.product.detail

import android.arch.lifecycle.MutableLiveData
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.repository.domain.products.ProductDetail

class ProductDetailViewModelController {
    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()

    //Data of this view-model
    var productDetailSource: MutableLiveData<ProductDetail>? = null
}