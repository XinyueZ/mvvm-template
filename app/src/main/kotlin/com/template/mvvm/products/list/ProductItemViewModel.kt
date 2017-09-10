package com.template.mvvm.products.list

import android.app.Application
import android.databinding.ObservableField
import com.template.mvvm.life.LifeViewModel

class ProductItemViewModel(app: Application) : LifeViewModel(app) {

    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

}