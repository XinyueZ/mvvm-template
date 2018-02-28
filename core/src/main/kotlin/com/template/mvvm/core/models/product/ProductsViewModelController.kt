package com.template.mvvm.core.models.product

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.BaseObservable
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.repository.domain.products.ProductList

class ProductsViewModelController : BaseObservable() {
    //Data of this view-model
    var collectionSource: ProductList? = null
    //For recyclerview data
    val collectionItemVmList: MutableLiveData<List<ViewModel>> = SingleLiveData()
    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()
    //Detail to open
    val openItemDetail: MutableLiveData<Long> = SingleLiveData()

}