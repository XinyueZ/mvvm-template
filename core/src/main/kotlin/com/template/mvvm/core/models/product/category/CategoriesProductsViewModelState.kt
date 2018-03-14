package com.template.mvvm.core.models.product.category

import android.databinding.BaseObservable
import android.databinding.ObservableBoolean

class CategoriesProductsViewModelState : BaseObservable() {
    val dataLoaded = ObservableBoolean(false)
    val dataHaveNotReloaded = ObservableBoolean(true)
    //Return this view to home
    val goBack = ObservableBoolean(false)
    //Delete list on UI
    val deleteList = ObservableBoolean(false)
}