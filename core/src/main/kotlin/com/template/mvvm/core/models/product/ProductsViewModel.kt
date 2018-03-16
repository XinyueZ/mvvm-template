package com.template.mvvm.core.models.product

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IntRange
import com.template.mvvm.core.ARG_SEL_ID
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.toViewModelList
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.Product
import com.template.mvvm.repository.domain.products.ProductList
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlin.coroutines.experimental.CoroutineContext

abstract class ProductsViewModel(protected val repository: ProductsDataSource) :
    AbstractViewModel() {
    val state = ProductsViewModelState()
    val controller = ProductsViewModelController()

    // Error
    var onError = ErrorViewModel()
    //User might delete data with pull2refresh and then the UI should also do it after new data being loaded,
    //ie. clean recyclerview and it's adapter.
    private var shouldDeleteList = false
    private var offset: Int = 0

    override fun onLifecycleStart() {
        with(controller) {
            lifecycleOwner.run {
                collectionSource = collectionSource ?: ProductList().toViewModelList(
                    this@run,
                    { ProductItemViewModel.newInstance(this, it) }) {
                    it?.let {
                        collectionItemVmList.value = it
                        showSystemUi.value = true
                        with(state) {
                            dataHaveNotReloaded.set(true)
                        }
                        bindTapHandlers(it)
                    }
                }

                collectionItemVmList.apply {
                    value = emptyList()
                }
            }
        }
    }

    override fun onLifecycleStop() {
        with(controller) {
            repository.clear()
            collectionSource = null
            state.deleteList.set(false)
            offset = 0
        }
    }

    private fun loadData() = onBound(0)

    fun onBound(@IntRange(from = 0L) position: Int) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        doOnBound(position)
    }

    private fun doOnBound(@IntRange(from = 0L) position: Int) = async(uiContext) {
        controller.collectionSource?.let { source ->
            if (position + 1 >= offset) {
                query(bgContext, offset).consumeEach { ds ->
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
        with(state) {
            if (shouldDeleteList) {
                deleteList.set(true)
                shouldDeleteList = false
                deleteList.set(false)
            }
        }
        source.value = list
    }

    protected open suspend fun query(
        coroutineContext: CoroutineContext,
        start: Int
    ) =
        repository.getAllProducts(coroutineContext, start, true)

    private fun reloadData() = doReloadData()

    private fun doReloadData() = async(uiContext) {
        delete(bgContext).consumeEach {
            offset = 0
            loadData()
            shouldDeleteList = true
        }
    }

    protected open suspend fun delete(coroutineContext: CoroutineContext) =
        repository.deleteAll(coroutineContext)

    private fun bindTapHandlers(it: List<ProductItemViewModel>) {
        it.forEach {
            it.clickHandler += { product, shared ->
                Pair(
                    Bundle().apply { putLong(ARG_SEL_ID, product.pid) },
                    shared
                ).also { controller.openItemDetail.value = it }
            }
        }
    }

    override fun onUiJobError(it: Throwable) {
        with(controller) {
            with(state) {
                showSystemUi.value = true
                dataHaveNotReloaded.set(true)
                onError.value = Error(it, R.string.error_load_all_products, R.string.error_retry) {
                    loadData()
                    showSystemUi.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        onLifecycleStop()
    }

    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onCommand(id: Int) {
        when (id) {
            R.id.action_app_bar_indicator -> state.goBack.set(true)
        }
    }

    fun onReload() {
        reloadData()
        state.dataHaveNotReloaded.set(false)
    }
    //-----------------------------------
}

class ProductItemViewModel : AbstractViewModel() {
    lateinit var product: Product
    val title: ObservableField<String> = ObservableField()
    val thumbnail: ObservableField<Uri> = ObservableField()
    val clickHandler = arrayListOf<((Product, Any?) -> Unit)>()

    companion object {
        fun newInstance(
            lifecycleOwner: LifecycleOwner,
            product: Product
        ): ProductItemViewModel {
            return ProductItemViewModel().apply {
                this.product = product
                title.set(product.title)
                thumbnail.set(product.pictures["Original"]?.uri)
                registerLifecycleOwner(lifecycleOwner)
            }
        }
    }

    fun onCommand(vm: ViewModel, shared: Any?) {
        clickHandler.first()(product, shared)
    }

    override fun onLifecycleStop() {
        super.onLifecycleStop()
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }
}