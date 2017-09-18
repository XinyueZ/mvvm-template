package com.template.mvvm.vm.models

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.databinding.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.binding.recycler.Binding
import com.template.mvvm.binding.recycler.RecyclerAdapter
import com.template.mvvm.data.repository.ProductsRepository
import com.template.mvvm.vm.AbstractViewModel
import com.template.mvvm.domain.SingleLiveData
import com.template.mvvm.domain.products.Product

class ProductsViewModel(app: Application, private val productsRepository: ProductsRepository) : AbstractViewModel(app) {

    val loadingText = ObservableInt(R.string.loading_products)
    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    // True when the data have been loaded.
    val pageStill = SingleLiveData<Boolean>()

    //For recyclerview data
    val productList = ObservableArrayList<ProductItemViewModel>()
    val listFactory = object : RecyclerAdapter.ViewBindingFactory {
        override fun create(type: Int, inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
            val types: Map<Int, Int> = mapOf(0 to R.layout.item_product)
            val layout = types[type]
            return DataBindingUtil.inflate<ViewDataBinding>(inflater, layout ?: FALLBACK_LAYOUT, parent, false)
        }
    }
    val listBinding = object : Binding.OnBind<ProductItemViewModel> {
        override fun onBind(binding: Binding<ProductItemViewModel>, position: Int, data: ProductItemViewModel) {
            binding.set(BR.vm, DEFAULT_BINDING_ITEM)
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
                }, { Toast.makeText(getApplication(), "Cannot load products.", Toast.LENGTH_SHORT).show() })
        )
        return true
    }
}

private const val DEFAULT_BINDING_ITEM = 0
private val FALLBACK_LAYOUT = R.layout.item_default

class ProductItemViewModel(app: Application) : AbstractViewModel(app) {

    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val thumbnail: ObservableField<Uri> = ObservableField()
    val brandLogo: ObservableField<Uri> = ObservableField()

    companion object {
        fun from(app: Application, product: Product): ProductItemViewModel {
            return ProductItemViewModel(app).apply {
                title.set(product.title)
                description.set(product.description)
                thumbnail.set(product.thumbnail)
                brandLogo.set(product.brandLogo)
            }
        }
    }

}
