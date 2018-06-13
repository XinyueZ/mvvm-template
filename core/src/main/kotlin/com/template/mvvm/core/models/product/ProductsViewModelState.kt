package com.template.mvvm.core.models.product

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.template.mvvm.core.R

class ProductsViewModelState : BaseObservable() {
    val title = ObservableInt(R.string.product_list_title)
    val dataHaveNotReloaded = ObservableBoolean(true)
    //Return this view to home
    val goBack = ObservableBoolean(false)
    //Delete list on UI
    val deleteList = ObservableBoolean(false)
}