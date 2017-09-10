package com.template.mvvm.products

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.products.list.ListBinding
import com.template.mvvm.products.list.ListViewFactory
import com.template.mvvm.products.list.ProductItemViewModel

class ProductsViewModel(app: Application) : LifeViewModel(app) {
    val title = ObservableInt(R.string.product_list_title)
    val goBack = ObservableBoolean(false)

    //For recyclerview data
    val productList = ObservableArrayList<ProductItemViewModel>()
    val listFactory = ListViewFactory()
    val listBinding = ListBinding()

    fun toggleBack() {
        goBack.set(true)
    }
}