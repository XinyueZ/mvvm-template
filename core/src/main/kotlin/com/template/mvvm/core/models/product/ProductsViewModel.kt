package com.template.mvvm.core.models.product

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import android.support.annotation.IntRange
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.core.ext.setUpTransform
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.Product
import com.template.mvvm.repository.domain.products.ProductList
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

open class ProductsViewModel(protected val repository: ProductsDataSource) : AbstractViewModel() {

    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

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
    //User might delete data with pull2refresh and then the UI should also do it after new data being loaded,
    //ie. clean recyclerview and it's adapter.
    private var shouldDeleteList = false

    //Detail to open
    val openItemDetail: MutableLiveData<Long> = SingleLiveData()

    private var offset: Int = 0

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        collectionSource = collectionSource ?: ProductList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
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

        collectionItemVmList.removeObservers(lifecycleOwner)
        collectionItemVmList.value = emptyList()
        return true
    }

    private fun loadData() = onBound(0)

    fun onBound(@IntRange(from = 0L) position: Int) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        doOnBound(position)
    }

    private fun doOnBound(@IntRange(from = 0L) position: Int) = launch(vmUiJob) {
        collectionSource?.let { source ->
            if (position >= offset - 1) {
                if (offset > 0) {
                    // For progress-loading for more items
                    moreLoaded.set(false)
                }
                query(offset).consumeEach { ds ->
                    ds?.takeIf { it.isNotEmpty() }?.let { list ->
                        offset += list.size
                        onQueried(source, list)
                    }
                }
            }
        }
    }

    private fun onQueried(
        source: ProductList,
        list: List<Product>
    ) {
        if (shouldDeleteList) {
            deleteList.set(true)
            shouldDeleteList = false
            deleteList.set(false)
        }
        source.value = list
    }

    protected open suspend fun query(start: Int) =
        repository.getAllProducts(CommonPool + vmJob, start, true)

    internal fun reloadData() {
        doReloadData()
    }

    private fun doReloadData() = launch(vmUiJob) {
        delete(CommonPool + vmJob).consumeEach {
            offset = 0
            loadData()
            shouldDeleteList = true
        }
    }

    protected open suspend fun delete(coroutineContext: CoroutineContext) =
        repository.deleteAll(coroutineContext)

    private fun bindTapHandlers(it: List<ProductItemViewModel>) {
        it.forEach {
            it.clickHandler += {
                // Tell UI to open a UI for license detail.
                openItemDetail.value = it.pid
            }
        }
    }

    override fun onUiJobError(it: Throwable) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)
        moreLoaded.set(true)

        onError.value = Error(it, R.string.error_load_all_products, R.string.error_retry) {
            loadData()
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
        reloadData()
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
