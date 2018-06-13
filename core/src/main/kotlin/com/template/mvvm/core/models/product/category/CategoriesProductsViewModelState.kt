package com.template.mvvm.core.models.product.category

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean

class CategoriesProductsViewModelState : BaseObservable() {
    val dataHaveNotReloaded = ObservableBoolean(true)
    //Return this view to home
    val goBack = ObservableBoolean(false)
    //Delete list on UI
    val deleteList = ObservableBoolean(false)
}