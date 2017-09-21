package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.data.source.ProductsRepository
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.ext.switchMapViewModelList
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ProductsViewModel(private val productsRepository: ProductsRepository) : AbstractViewModel() {

    val loadingText = ObservableInt(R.string.loading_products)
    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    // True when the data have been loaded.
    val pageStill = SingleLiveData<Boolean>()

    // Error
    var onError = ErrorViewModel()

    //For recyclerview data
    val productList = ObservableArrayList<ProductItemViewModel>()
    val itemBinding = ItemBinding.of<ProductItemViewModel>(BR.vm, R.layout.item_product)

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        loadAllProducts(lifecycleOwner)
        return true
    }

    private fun loadAllProducts(lifecycleOwner: LifecycleOwner) {
        addToAutoDispose(
                productsRepository.getAllProducts(lifecycleOwner).doFinally {
                    onLoadProductsCompletely()
                }.subscribe(
                        {
                            loadProductsSuccessfully(it, lifecycleOwner)
                        },
                        {
                            canNotLoadProducts(it, lifecycleOwner)
                        }
                )
        )
    }

    private fun onLoadProductsCompletely() {
        dataLoaded.set(true)
    }

    private fun canNotLoadProducts(it: Throwable, lifecycleOwner: LifecycleOwner) {
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllProducts(lifecycleOwner)
            pageStill.value = false
            dataLoaded.set(false)
        }
    }

    private fun loadProductsSuccessfully(it: ProductList, lifecycleOwner: LifecycleOwner) {
        it.switchMapViewModelList(lifecycleOwner)
        {
            it?.let {
                productList.addAll(it)
                pageStill.value = true
            }
        }
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
