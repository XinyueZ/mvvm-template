package com.template.mvvm.core.models.product

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.core.ext.setUpTransform
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.repository.LL
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.Product
import com.template.mvvm.repository.domain.products.ProductList
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

open class ProductsViewModel(protected val repository: ProductsDataSource) : AbstractViewModel() {

    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    private val reload = SingleLiveData<Boolean>()
    val dataHaveNotReloaded = ObservableBoolean(true)
    val moreLoaded = ObservableBoolean(true)

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    //Data of this view-model
    private var collectionSource: ProductList? = null

    //For recyclerview data
    val collectionItemVmList: MutableLiveData<List<ViewModel>> = SingleLiveData()

    //Delete list on UI
    val deleteList = ObservableBoolean(false)
    //User might delete data with pull2refresh and then the UI should also do it after new data being loaded.
    private var shouldDeleteList = false

    //Detail to open
    val openItemDetail: MutableLiveData<Long> = SingleLiveData()

    private var offset: Int = 0

    lateinit var lifecycleOwner: LifecycleOwner

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        this.lifecycleOwner = lifecycleOwner
        reload.observe(lifecycleOwner, Observer {
            deleteProducts()
        })
        collectionSource = collectionSource ?: ProductList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    LL.d("Updating new data...")
                    collectionItemVmList.value = it

                    showSystemUi.value = true
                    dataLoaded.set(true)
                    moreLoaded.set(true)
                    dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                    dataHaveNotReloaded.set(true)

                    bindTapHandlers(it)
                }
            }
        }
        loadAllProducts()
        return true
    }

    private fun loadAllProducts() {
        onBound(0)
    }

    fun onBound(position: Int) {
        launch(UI + CoroutineExceptionHandler({ _, e ->
            canNotLoadProducts(e)
            LL.d(e.message ?: "")
        }) + vmJob) {
            collectionSource?.let { source ->
                LL.d("offset = $offset, position = $position")
                if (position >= offset - 1) {
                    LL.i("Load next from $position")
                    if (offset > 0) {
                        // For progress-loading for more items
                        moreLoaded.set(false)
                    }
                    query(offset).consumeEach { ds ->
                        LL.i("collectionSource next subscribe")
                        ds?.takeIf { it.isNotEmpty() }?.let {

                            if (shouldDeleteList) {
                                deleteList.set(true)
                                shouldDeleteList = false
                                deleteList.set(false)
                            }

                            source.value = it
                            offset += it.size
                        }
                    }
                }
            }
        }
    }

    private fun deleteProducts() = launch(UI + CoroutineExceptionHandler({ _, e ->
        LL.d(e.message ?: "")
    }) + vmJob) {
        delete().consumeEach {
            LL.i("deleted products...")
            offset = 0
            loadAllProducts()
            shouldDeleteList = true
        }
    }

    protected open suspend fun query(start: Int) = repository.getAllProducts(vmJob, start, true)

    protected open suspend fun delete() = repository.deleteAll(vmJob)

    private fun bindTapHandlers(it: List<ProductItemViewModel>) {
        it.forEach {
            it.clickHandler += {
                // Tell UI to open a UI for license detail.
                openItemDetail.value = it.pid
            }
        }
    }

    private fun canNotLoadProducts(it: Throwable) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)
        moreLoaded.set(true)

        onError.value = Error(it, R.string.error_load_all_products, R.string.error_retry) {
            loadAllProducts()
            showSystemUi.value = false

            //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
            collectionSource?.value?.let {
                dataLoaded.set(it.isNotEmpty())
            } ?: dataLoaded.set(false)
        }
    }

    fun reset() {
        repository.clear()
        collectionSource = null
        collectionItemVmList.removeObservers(lifecycleOwner)
        deleteList.set(false)
        offset = 0
    }

    override fun onCleared() {
        super.onCleared()
        reset()
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

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }
}
