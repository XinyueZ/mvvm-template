package com.template.mvvm.data.local

import com.template.mvvm.data.ProductsDataSource
import com.template.mvvm.data.domain.products.Product
import com.template.mvvm.data.domain.products.ProductList
import io.reactivex.Single

class ProductsLocal : ProductsDataSource {
    override fun getAllProducts(): Single<ProductList> {
        return Single.create({ emitter ->
            with(ProductList()) {
                value = arrayListOf(
                        Product("BIOM", "BIOM FJUEL - Trainers - aquatic"),
                        Product("FOGGY", "FOGGY - Trainers - brown/beige"),
                        Product("Sports", "Sports socks - blue"),
                        Product("PALERMO", "PALERMO - Trainers - oliv/rost"),
                        Product("JFWLAFAYETTE", "JFWLAFAYETTE  - Trainers - ivy green"),
                        Product("STRIKER", "STRIKER - Trainers - dress blues"),
                        Product("JFWLAFAYETTE", "JFWLAFAYETTE - Trainers - anthracite"),
                        Product("BILBAO II SUN", "BILBAO II SUN - Trainers - blue/lime"),
                        Product("BILBAO II SUN", "BILBAO II SUN - Trainers - black/white"),
                        Product("PICK", "PICK POCKET TX - Trainers - black")
                )
                if (!emitter.isDisposed)
                    emitter.onSuccess(this)
            }
        })
    }
}