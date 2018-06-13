package com.template.mvvm.core.models.product.detail

import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.repository.domain.products.ProductDetail

class ProductDetailViewModelController {
    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> =
        SingleLiveData()

    //Data of this view-model
    var productDetailSource: MutableLiveData<ProductDetail>? = null

    var palette = SingleLiveData<Palette>()
}