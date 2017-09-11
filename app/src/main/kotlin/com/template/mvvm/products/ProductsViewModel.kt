package com.template.mvvm.products

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.os.Handler
import android.util.Log
import com.template.mvvm.R
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.domain.Product
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.products.list.ListBinding
import com.template.mvvm.products.list.ListViewFactory
import com.template.mvvm.products.list.ProductItemViewModel
import com.template.mvvm.products.msg.LoadProductList

class ProductsViewModel(app: Application) : LifeViewModel(app) {
    private val TAG = "ProductsViewModel"

    val title = ObservableInt(R.string.product_list_title)
    val goBack = ObservableBoolean(false)

    //For recyclerview data
    val productList = ObservableArrayList<ProductItemViewModel>()
    val listFactory = ListViewFactory()
    val listBinding = ListBinding()

    fun toggleBack() {
        goBack.set(true)
    }

    private fun loadProductList(msg: Message<Any>) {
        val data = arrayListOf(
                ProductItemViewModel.from(getApplication(), Product("BIOM", "BIOM FJUEL - Trainers - aquatic")),
                ProductItemViewModel.from(getApplication(), Product("FOGGY", "FOGGY - Trainers - brown/beige")),
                ProductItemViewModel.from(getApplication(), Product("Sports", "Sports socks - blue")),
                ProductItemViewModel.from(getApplication(), Product("PALERMO", "PALERMO - Trainers - oliv/rost")),
                ProductItemViewModel.from(getApplication(), Product("JFWLAFAYETTE", "JFWLAFAYETTE  - Trainers - ivy green")),
                ProductItemViewModel.from(getApplication(), Product("STRIKER", "STRIKER - Trainers - dress blues")),
                ProductItemViewModel.from(getApplication(), Product("JFWLAFAYETTE", "JFWLAFAYETTE - Trainers - anthracite")),
                ProductItemViewModel.from(getApplication(), Product("BILBAO II SUN", "BILBAO II SUN - Trainers - blue/lime")),
                ProductItemViewModel.from(getApplication(), Product("BILBAO II SUN", "BILBAO II SUN - Trainers - black/white")),
                ProductItemViewModel.from(getApplication(), Product("PICK", "PICK POCKET TX - Trainers - black"))
        )
        productList.addAll(data)
    }

    private fun onActorError(error: Throwable) {
        Log.d(TAG, "onActorError $error")
    }

    override fun registerLifecycleOwner(lifecycleRegistryOwner: LifecycleRegistryOwner): Boolean {
        Interactor.start(lifecycleRegistryOwner)
                .subscribe(LoadProductList::class, this::loadProductList)
                .subscribeError(this::onActorError)
                .register()

        Handler().postDelayed({
            Interactor.post(LoadProductList("Load products into a list"))
        }, 1500)
        return true
    }
}