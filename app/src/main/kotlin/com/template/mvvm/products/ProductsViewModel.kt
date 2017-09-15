package com.template.mvvm.products

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.widget.Toast
import com.template.mvvm.R
import com.template.mvvm.data.ProductsRepository
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData
import com.template.mvvm.products.list.ListBinding
import com.template.mvvm.products.list.ListViewFactory
import com.template.mvvm.products.list.ProductItemViewModel

class ProductsViewModel(app: Application, private val productsRepository: ProductsRepository = ProductsRepository()) : LifeViewModel(app) {

    val loadingText = ObservableInt(R.string.loading_products)
    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    // True when the data have been loaded.
    internal val pageStill = SingleLiveData<Boolean>()

    //For recyclerview data
    val productList = ObservableArrayList<ProductItemViewModel>()
    val listFactory = ListViewFactory()
    val listBinding = ListBinding()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    override fun registerLifecycleOwner(lifecycleRegistryOwner: LifecycleRegistryOwner): Boolean {
        addToAutoDispose(
                productsRepository.getAllProducts().subscribe({
                    it.switchMapViewModelList(lifecycleRegistryOwner) {
                        it?.let {
                            productList.addAll(it)
                            dataLoaded.set(true)
                            pageStill.value = true
                        }
                    }
                }, { Toast.makeText(getApplication(), "Cannot load products.", Toast.LENGTH_SHORT).show() })
        )
        return true
    }
}