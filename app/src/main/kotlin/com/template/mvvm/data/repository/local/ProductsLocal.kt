package com.template.mvvm.data.local

import android.os.Handler
import com.template.mvvm.data.domain.products.Product
import com.template.mvvm.data.domain.products.ProductList
import com.template.mvvm.data.repository.ProductsDataSource
import io.reactivex.Single

class ProductsLocal : ProductsDataSource {
    private val productList = ProductList()

    override fun getAllProducts(): Single<ProductList> {
        return Single.create({ emitter ->
            with(productList) {
                loadProducts()
                if (!emitter.isDisposed)
                    emitter.onSuccess(this)
            }
        })
    }

    private fun loadProducts() {
        Handler().postDelayed({
            productList.value = arrayListOf(
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
        }, 5000)
    }
}