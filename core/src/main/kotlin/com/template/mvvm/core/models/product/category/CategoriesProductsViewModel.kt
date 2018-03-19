package com.template.mvvm.core.models.product.category

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.os.Bundle
import android.support.annotation.IntRange
import com.template.mvvm.core.ARG_SEL_ID
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.toViewModelList
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.core.models.product.CategoryProductsViewModel
import com.template.mvvm.core.models.product.ProductItemViewModel
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.ProductCategory
import com.template.mvvm.repository.domain.products.ProductCategoryList
import kotlinx.coroutines.experimental.async
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
                            ProductCategoryItemViewModel.newInstance(
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

    fun onBound(@IntRange(from = 0L) position: Int) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        doOnBound(position)
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
        source.value = it
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

class ProductCategoryItemViewModel : AbstractViewModel() {
    val name: ObservableField<String> = ObservableField()
    var categoryProductsViewModel = ObservableField<CategoryProductsViewModel>()

    companion object {
        fun newInstance(
            lifecycleOwner: LifecycleOwner,
            repository: ProductsDataSource,
            productCategory: ProductCategory,
            openItemDetail: MutableLiveData<Pair<Bundle, Any?>>
        ): ProductCategoryItemViewModel {
            return ProductCategoryItemViewModel()
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
                        .also { it.controller.openItemDetail = openItemDetail}
                        .also { it.registerLifecycleOwner(lifecycleOwner) /*load products of category(item)*/ }
                }
        }
    }
}