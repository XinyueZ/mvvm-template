package com.template.mvvm.data.remote

import android.os.Handler
import com.template.mvvm.data.ProductsDataSource
import com.template.mvvm.data.domain.ProductList
import com.template.mvvm.domain.Product
import com.template.mvvm.utils.LL

class ProductsRemote : ProductsDataSource {
    val list = ProductList()

    override fun getAllProducts(): ProductList {
        LL.d("ProductsRemote::getAllProducts")
        Handler().postDelayed({
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
                    Product("PICK", "PICK POCKET TX - Trainers - black")
            )
        }, 5000)
        return list
    }
}