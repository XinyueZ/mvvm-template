package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
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
    private var productListSource: ProductList? = null

    //For recyclerview data
    private val backProductItemVmList = SingleLiveData<List<ViewModel>>()
    val productItemVmList: ObservableField<LiveData<List<ViewModel>>> = ObservableField(backProductItemVmList)

    //Detail to open
    val openProductDetail: MutableLiveData<Long> = SingleLiveData()

    lateinit var lifecycleOwner: LifecycleOwner

    private var offset: Int = 0

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        this.lifecycleOwner = lifecycleOwner
        productListSource = productListSource ?: ProductList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    LL.d("Updating new data...")
                    backProductItemVmList.value = it

                    showSystemUi.value = true
                    dataLoaded.set(true)
                    dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                    dataHaveNotReloaded.set(true)

                    bindTapHandlers(it)
                }
            }
        }
        reload.observe(lifecycleOwner, Observer {
            loadAllProducts()
        })
        loadAllProducts()
        return true
    }

    protected open fun loadAllProducts() {
        onListItemBound(0)
    }

    fun onListItemBound(position: Int) {
        launch(UI + CoroutineExceptionHandler({ _, e ->
            canNotLoadProducts(e)
            LL.d(e.message ?: "")
        }) + vmJob) {
            productListSource?.let { source ->
                LL.d("offset = $offset, position = $position")
                if (position >= offset - 1) {
                    LL.i("Load next from $position")
                    queryProducts(offset).consumeEach { ds ->
                        LL.i("productListSource next subscribe")
                        ds?.takeIf { it.isNotEmpty() }?.let {
                            source.value = it
                            offset += it.size
                        }
                    }
                }
            }
        }
    }

    protected open suspend fun queryProducts(start: Int) = repository.getAllProducts(vmJob, start, true)

    private fun bindTapHandlers(it: List<ProductItemViewModel>) {
        it.forEach {
            it.clickHandler += {
                // Tell UI to open a UI for license detail.
                openProductDetail.value = it.pid
            }
        }
    }

    private fun canNotLoadProducts(it: Throwable) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)


        onError.value = Error(it, R.string.error_load_all_products, R.string.error_retry) {
            loadAllProducts()
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
        offset = 0
    }
    //-----------------------------------
}

class ProductItemViewModel : AbstractViewModel() {
    lateinit var product: Product
    val title: ObservableField<String> = ObservableField()
    val thumbnail: ObservableField<Uri> = ObservableField()
    val clickHandler = arrayListOf<((Product) -> Unit)>()

    companion object {
        fun from(product: Product): ProductItemViewModel {
            return ProductItemViewModel().apply {
                this.product = product
                title.set(product.title)
                thumbnail.set(product.pictures["Original"]?.uri)
            }
        }
    }

    fun onCommand(vm: ViewModel) {
        clickHandler.first()(product)
    }

    /***
     * For MvvmListAdapter.diffCallback
     */
    override fun equals(other: Any?) =
            if (other == null) false
            else product.pid == ((other as ProductItemViewModel).product.pid)

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }
}
