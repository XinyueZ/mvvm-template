package com.template.mvvm.data.source.local

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Single

class ProductsLocal : ProductsDataSource {
    private var productList: ProductList? = null

    override fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList> {
        val ret: Single<ProductList> = Single.create({ emitter ->
            productList = (productList ?: ProductList()).apply {
                loadProducts(this)
                if (!emitter.isDisposed)
                    emitter.onSuccess(this)
            }
        })
        ret.doFinally({
            clear()
        })
        return ret
    }

    private fun loadProducts(list: ProductList) {
        list.value = arrayListOf(
                Product("BIOM", "BIOM FJUEL - Trainers - aquatic"),
                Product("FOGGY", "FOGGY - Trainers - brown/beige"),
                Product("Sports", "Sports socks - blue"),
                Product("PALERMO", "PALERMO - Trainers - oliv/rost"),
                Product("JFWLAFAYETTE", "JFWLAFAYETTE  - Trainers - ivy green"),
                Product("STRIKER", "STRIKER - Trainers - dress blues"),
                Product("JFWLAFAYETTE", "JFWLAFAYETTE - Trainers - anthracite"),
                Product("BILBAO II SUN", "BILBAO II SUN - Trainers - blue/lime"),
                Product("BILBAO II SUN", "BILBAO II SUN - Trainers - black/white"),
                Product("PICK", "PICK POCKET TX - Trainers - black"))
    }

    override fun clear() {
        productList = null
    }
}