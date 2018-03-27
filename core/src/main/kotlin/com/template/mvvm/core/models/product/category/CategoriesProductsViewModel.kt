package com.template.mvvm.core.models.product.category

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.os.Bundle
import android.support.annotation.IntRange
import com.template.mvvm.base.utils.LL
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.AbstractViewModel
import com.template.mvvm.core.arch.registerLifecycleOwner
import com.template.mvvm.core.arch.toViewModelList
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.core.models.product.CategoryProductsViewModel
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.ProductCategory
import com.template.mvvm.repository.domain.products.ProductCategoryList
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlin.coroutines.experimental.CoroutineContext

open class CategoriesProductsViewModel(private val repository: ProductsDataSource) :
    AbstractViewModel() {
    val controller = CategoriesProductsViewModelController()
    val state = CategoriesProductsViewModelState()
    // Error
    var onError = ErrorViewModel()
    //User might delete data with pull2refresh and then the UI should also do it after new data being loaded,
    //ie. clean recyclerview and it's adapter.
    private var shouldDeleteList = false

    override fun onLifecycleStart() {
        with(controller) {
            lifecycleOwner.run {
                productCategoryListSource = productCategoryListSource ?:
                        ProductCategoryList().toViewModelList(this@run, {
                            CategoryProductItemViewModel.newInstance(
                                this,
                                repository,
                                it,
                                openItemDetail
                            )
                        }) {
                            it?.let {
                                productCategoryItemVmList.value = it
                                with(state) {
                                    dataHaveNotReloaded.set(true)
                                }
                            }
                        }
            }
            productCategoryItemVmList.value = emptyList()
        }
    }

    override fun onLifecycleStop() {
        with(controller) {
            repository.clear()
            productCategoryListSource = null
            state.deleteList.set(false)
        }
    }

    private fun loadData() = onBound(0)

    fun onUnbound(@IntRange(from = 0L) position: Int, vm: ViewModel? = null) {
        (vm as? CategoryProductItemViewModel)?.onUnbound()
    }

    fun onBound(@IntRange(from = 0L) position: Int, vm: ViewModel? = null) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        doOnBound(position)
    }

    fun onShown(position: Int, vm: ViewModel? = null) {
        if (position > 0) (vm as? CategoryProductItemViewModel)?.onShown()
    }

    private fun doOnBound(@IntRange(from = 0L) position: Int) = async(uiContext) {
        controller.productCategoryListSource?.let { source ->
            query(bgContext, position).consumeEach { ds ->
                ds?.takeIf { it.isNotEmpty() }?.let { list ->
                    onQueried(source, list)
                }
            }
        }
    }

    private fun onQueried(
        source: ProductCategoryList,
        it: List<ProductCategory>?
    ) {
        with(state) {
            if (shouldDeleteList) {
                deleteList.set(true)
                shouldDeleteList = false
                deleteList.set(false)
            }
        }
        source.value = it?.filter {
            it.cid == "women" || it.cid == "makeup" ||
                    it.cid == "womens-clothes" || it.cid == "mens-clothes"
        }
        LL.d("${source.value}")
    }

    private suspend fun query(
        coroutineContext: CoroutineContext,
        start: Int
    ) = repository.getProductCategories(coroutineContext, start, true)

    private fun reloadData() = doReloadData()

    private fun doReloadData() = async(uiContext) {
        delete(bgContext).consumeEach {
            loadData()
            shouldDeleteList = true
        }
    }

    private suspend fun delete(coroutineContext: CoroutineContext) =
        repository.deleteProductCategories(coroutineContext)

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

class CategoryProductItemViewModel : AbstractViewModel() {
    val name: ObservableField<String> = ObservableField()
    var categoryProductsViewModel = ObservableField<CategoryProductsViewModel>()

    fun onUnbound() {
        categoryProductsViewModel.get()?.apply {
            LL.d("onCleared, delete line list: ${name.get()}")
            bgContext.cancel()
            uiContext.cancel()
//            state.deleteList.set(true)
        }
    }

    fun onShown() {
        LL.d("---------------------------")
        LL.d("Category: ${name.get()}")
        LL.d("List: ${categoryProductsViewModel.get()?.controller?.collectionSource?.value?.size}")
        LL.d("---------------------------")
//        categoryProductsViewModel.get()?.onBound(0)
    }

    companion object {
        fun newInstance(
            lifecycleOwner: LifecycleOwner,
            repository: ProductsDataSource,
            productCategory: ProductCategory,
            openItemDetail: MutableLiveData<Pair<Bundle, Any?>>
        ) = CategoryProductItemViewModel()
            .also { it.registerLifecycleOwner(lifecycleOwner) }
            .also {
                it.name.set(productCategory.name)
            }
            .also { item ->
                /**
                 * The product-list of each category.
                 */
                CategoryProductsViewModel(repository, productCategory.cid)
                    .also { item.categoryProductsViewModel.set(it) /*bind*/ }
                    .also { it.controller.openItemDetail = openItemDetail }
                    .also { it.registerLifecycleOwner(lifecycleOwner) /*load products of category(item)*/ }
            }
    }
}