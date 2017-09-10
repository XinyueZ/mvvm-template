package com.template.mvvm.products

import android.app.Application
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel

class ProductsViewModel(app: Application) : LifeViewModel(app) {
    val title = ObservableInt(R.string.product_list_title)
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }
}