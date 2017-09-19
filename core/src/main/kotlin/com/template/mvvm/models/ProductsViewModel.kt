package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.databinding.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.binding.recycler.Binding
import com.template.mvvm.binding.recycler.RecyclerAdapter
import com.template.mvvm.data.source.ProductsRepository
import com.template.mvvm.databinding.ProductItemBinding
import com.template.mvvm.domain.products.Product
import com.template.mvvm.ext.switchMapViewModelList

class ProductsViewModel(private val productsRepository: ProductsRepository) : AbstractViewModel() {

    val loadingText = ObservableInt(R.string.loading_products)
    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    // True when the data have been loaded.
    val pageStill = SingleLiveData<Boolean>()

    //For recyclerview data
    val productList = ObservableArrayList<ProductItemViewModel>()
    val listFactory = object : RecyclerAdapter.ViewBindingFactory {
        override fun create(type: Int, inflater: LayoutInflater, parent: ViewGroup) = DataBindingUtil.inflate<ProductItemBinding>(inflater, R.layout.item_product, parent, false)
    }
    val listBinding = object : Binding.OnBind<ProductItemViewModel> {
        override fun onBind(binding: Binding<ProductItemViewModel>, position: Int, data: ProductItemViewModel) {
            binding.set(BR.vm, 0)
        }
    }

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        addToAutoDispose(
                productsRepository.getAllProducts(lifecycleOwner).subscribe({
                    it.switchMapViewModelList(lifecycleOwner) {
                        it?.let {
                            productList.addAll(it)
                            dataLoaded.set(true)
                            pageStill.value = true
                        }
                    }
                },
                        {
                            //TODO Error-handling
                        })
        )
        return true
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
