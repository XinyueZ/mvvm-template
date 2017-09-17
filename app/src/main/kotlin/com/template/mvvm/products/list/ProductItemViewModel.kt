package com.template.mvvm.products.list

import android.app.Application
import android.databinding.ObservableField
import android.net.Uri
import com.template.mvvm.data.domain.products.Product
import com.template.mvvm.life.LifeViewModel

class ProductItemViewModel(app: Application) : LifeViewModel(app) {

    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val thumbnail: ObservableField<Uri> = ObservableField()
    val brandLogo: ObservableField<Uri> = ObservableField()


    companion object {
        fun from(app: Application, product: Product): ProductItemViewModel {
            return ProductItemViewModel(app).apply {
                title.set(product.title)
                description.set(product.description)
                thumbnail.set(product.thumbnail)
                brandLogo.set(product.brandLogo)
            }
        }
    }

}