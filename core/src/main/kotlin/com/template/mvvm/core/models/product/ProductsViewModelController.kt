package com.template.mvvm.core.models.product

import android.os.Bundle
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.repository.domain.products.ProductList

class ProductsViewModelController : BaseObservable() {
    //Data of this view-model
    var collectionSource: ProductList? = null
    //For recyclerview data
    val collectionItemVmList = MutableLiveData<List<ViewModel>>()
    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> =
        SingleLiveData()
    //Detail to open
    var openItemDetail: MutableLiveData<Pair<Bundle, Any?>> =
        SingleLiveData()

}