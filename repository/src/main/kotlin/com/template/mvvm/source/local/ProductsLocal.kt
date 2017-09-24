package com.template.mvvm.source.local

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts(source: ProductList) = Completable.create { sub ->
        loadProducts(source)
        sub.onComplete()
        return@create
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

    override fun saveListOfProduct(listOfProduct: List<Product>) = Completable.create { sub ->
        listOfProduct.forEach {
            DB.INSTANCE.productDao().insertProduct(
                    ProductEntity.from(it)
            )
        }
        sub.onComplete()
        return@create
    }.subscribeOn(Schedulers.io())

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}