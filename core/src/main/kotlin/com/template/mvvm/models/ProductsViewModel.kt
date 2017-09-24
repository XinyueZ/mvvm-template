package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import com.template.mvvm.BR
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.ext.setUpTransform
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ProductsViewModel(private val productsRepository: ProductsDataSource) : AbstractViewModel() {

    val loadingText = ObservableInt(R.string.loading_products)
    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    // True when the data have been loaded.
    val pageStill = MutableLiveData<Boolean>()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    //Data of this view-model
    private var productListSource: ProductList? = null

    //For recyclerview data
    val productItemVmList = ObservableArrayList<ProductItemViewModel>()
    val itemBinding = ItemBinding.of<ProductItemViewModel>(BR.vm, R.layout.item_product)

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        productListSource = productListSource ?: ProductList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    productItemVmList.addAll(it)
                    pageStill.value = true
                }
            }

            observe(lifecycleOwner, Observer {
                value?.let {
                    addToAutoDispose(productsRepository.saveListOfProduct(it).subscribe())
                }
            })
        }
        loadAllProducts(lifecycleOwner)
        return true
    }

    private fun loadAllProducts(lifecycleOwner: LifecycleOwner) {
        productListSource?.let {
            addToAutoDispose(
                    productsRepository.getAllProducts(it).doFinally {
                        onLoadProductsCompletely()
                    }.subscribe({}, { canNotLoadProducts(it, lifecycleOwner) })
            )
        }
    }

    private fun onLoadProductsCompletely() {
        dataLoaded.set(true)
    }

    private fun canNotLoadProducts(it: Throwable, lifecycleOwner: LifecycleOwner) {
        LL.e(it.toString())
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllProducts(lifecycleOwner)
            pageStill.value = false
            dataLoaded.set(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        productListSource = null
    }
}

class ProductItemViewModel : AbstractViewModel() {

    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val thumbnail: ObservableField<Uri> = ObservableField()
    val brandLogo: ObservableField<Uri> = ObservableField()

    companion object {
        fun from(product: Product): ProductItemViewModel {
            return ProductItemViewModel().apply {
                title.set(product.title)
                description.set(product.description)
                thumbnail.set(product.thumbnail)
                brandLogo.set(product.brandLogo)
            }
        }
    }

}
