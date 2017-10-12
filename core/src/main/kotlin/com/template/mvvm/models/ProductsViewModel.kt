package com.template.mvvm.models

import android.arch.lifecycle.*
import android.arch.paging.PagedList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import android.text.TextUtils
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.arch.recycler.MvvmListDataProvider
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.ext.setUpTransform
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

open class ProductsViewModel(protected val repository: ProductsDataSource) : AbstractViewModel() {

    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    private val reload = SingleLiveData<Boolean>()
    val dataHaveNotReloaded = ObservableBoolean(true)

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    //Data of this view-model
    protected var productListSource: ProductList? = null

    //For recyclerview data
    val productItemVmList: ObservableField<LiveData<PagedList<ViewModel>>> = ObservableField()

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        productListSource = productListSource ?: ProductList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    productItemVmList.set(
                            MvvmListDataProvider(it).create(
                                    0,
                                    PagedList.Config.Builder()
                                            .setPageSize(it.size)
                                            .setInitialLoadSizeHint(it.size)
                                            .setEnablePlaceholders(true)
                                            .build())
                    )

                    showSystemUi.value = true
                    dataLoaded.set(true)
                    dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                    dataHaveNotReloaded.set(true)
                }
            }
        }
        reload.observe(lifecycleOwner, Observer {
            loadAllProducts(lifecycleOwner, false)
        })
        loadAllProducts(lifecycleOwner)
        return true
    }

    protected open fun loadAllProducts(lifecycleOwner: LifecycleOwner, localOnly: Boolean = true) {
        productListSource?.let {
            launch(UI + CoroutineExceptionHandler({ _, e ->
                canNotLoadProducts(e, lifecycleOwner)
                LL.d(e.message ?: "")
            }) + vmJob) {
                repository.getAllProducts(vmJob, localOnly).consumeEach {
                    LL.i("productListSource subscribe")
                    productListSource?.value = it
                }
            }
        }
    }

    protected fun canNotLoadProducts(it: Throwable, lifecycleOwner: LifecycleOwner) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)


        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllProducts(lifecycleOwner, false)
            showSystemUi.value = false

            //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
            productListSource?.value?.let {
                dataLoaded.set(it.isNotEmpty())
            } ?: dataLoaded.set(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
        productListSource = null
    }

    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onCommand(id: Int) {
        when (id) {
            R.id.action_app_bar_indicator -> goBack.set(true)
        }
    }

    fun onReload() {
        reload.value = true
        dataHaveNotReloaded.set(false)
    }
    //-----------------------------------
}

class ProductItemViewModel : AbstractViewModel() {
    lateinit var product: Product
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val thumbnail: ObservableField<Uri> = ObservableField()
    val brandLogo: ObservableField<Uri> = ObservableField()

    companion object {
        fun from(product: Product): ProductItemViewModel {
            return ProductItemViewModel().apply {
                this.product = product
                title.set(product.title)
                description.set(product.description)
                thumbnail.set(product.thumbnail)
                brandLogo.set(product.brand.logo)
            }
        }
    }

    override fun equals(other: Any?) =
            if (other == null) false
            else TextUtils.equals(product.pid, ((other as ProductItemViewModel).product.pid))
}
