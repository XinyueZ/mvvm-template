package com.template.mvvm.core.models.product

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.template.mvvm.core.ARG_SEL_ID
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.AbstractViewModel
import com.template.mvvm.core.arch.registerLifecycleOwner
import com.template.mvvm.core.arch.toViewModelList
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
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
                collectionItemVmList.value = emptyList()
            }
        }
    }

    override fun onLifecycleStop() {
        with(controller) {
            repository.clear()
            collectionSource = null
            state.deleteList.set(false)
        }
    }

    private fun loadData() = onBound(0)

    fun onBound(position: Int, vm: ViewModel? = null) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        doOnBound(position)
    }

    private fun doOnBound(position: Int) = async(uiContext) {
        controller.collectionSource?.let { source ->
            query(bgContext, position).consumeEach { ds ->
                ds?.takeIf { it.isNotEmpty() }?.let { list ->
                    onQueried(source, list)
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

    fun onCommand(vm: ViewModel, shared: Any?) {
        clickHandler.first()(product, shared)
    }

    override fun onLifecycleDestroy() {
        super.onLifecycleDestroy()
        clickHandler.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }

    companion object {
        fun newInstance(
            lifecycleOwner: LifecycleOwner,
            product: Product
        ) = ProductItemViewModel().apply {
            registerLifecycleOwner(lifecycleOwner)
            this.product = product
            title.set(product.title)
            thumbnail.set(product.pictures["Original"]?.uri)
        }

    }
}