package com.template.mvvm.core.models.product.category

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.support.annotation.IntRange
import com.template.mvvm.base.utils.LL
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.toViewModelList
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.core.models.product.CategoryProductsViewModel
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.ProductCategory
import com.template.mvvm.repository.domain.products.ProductCategoryList
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
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
    private var offset: Int = 0

    override fun onLifecycleStart() {
        with(controller) {
            lifecycleOwner.run {
                productCategoryListSource = productCategoryListSource ?:
                        ProductCategoryList().toViewModelList(this@run, {
                            ProductCategoryItemViewModel.from(
                                this,
                                repository,
                                it
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
            productCategoryItemVmList.apply {
                value = emptyList()

            }
        }
    }

    override fun onLifecycleStop() {
        with(controller) {
            repository.clear()
            productCategoryListSource = null
            state.deleteList.set(false)
            offset = 0
        }
    }

    private fun loadData() = onBound(0)

    private fun reloadData() = doReloadData()

    private fun doReloadData() = async(uiContext) {
        produce<Unit>(bgContext) {
            /**
             * Simulate delete operation
             */
            delay(1500)
            send(Unit)
        }.consumeEach {
            state.deleteList.set(true)

            offset = 0
            loadData()
            shouldDeleteList = true
        }
    }

    fun onBound(@IntRange(from = 0L) position: Int) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        when (controller.productCategoryItemVmList.value?.isEmpty() ?: kotlin.run { true }) {
            true -> doOnBound(position)
            else -> controller.productCategoryListSource?.value = emptyList()
        }
    }

    private fun doOnBound(@IntRange(from = 0L) position: Int) = async(uiContext) {
        controller.productCategoryListSource?.let { source ->
            if (position + 1 >= offset) {
                query(bgContext, offset).consumeEach {
                    onQueried(source, it)
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
    ) =
        produce(coroutineContext) {
            send(
                listOf(
                    ProductCategory("mens-clothes", "Men's Clothes"),
                    ProductCategory("mens-athletic", "Men's Activewear"),
                    ProductCategory("womens-clothes", "Women's Clothes"),
                    ProductCategory("womens-athletic-clothes", "Women's Athletic Clothes")
                )
            )
        }

    override fun onUiJobError(it: Throwable) {
        with(state) {
            with(controller) {
                dataHaveNotReloaded.set(true)
                onError.value = Error(
                    it,
                    R.string.error_load_all_products_categories,
                    R.string.error_retry
                ) {
                    reloadData()
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
    lateinit var productCategory: ProductCategory
    val name: ObservableField<String> = ObservableField()
    private val clickHandler = arrayListOf<((ProductCategory) -> Unit)>()
    var categoryProductsViewModel = ObservableField<CategoryProductsViewModel>()

    companion object {
        fun from(
            lifecycleOwner: LifecycleOwner,
            repository: ProductsDataSource,
            productCategory: ProductCategory
        ): ProductCategoryItemViewModel {
            return ProductCategoryItemViewModel().apply {
                registerLifecycleOwner(lifecycleOwner)

                this.productCategory = productCategory
                name.set(productCategory.name)

                categoryProductsViewModel.set(
                    CategoryProductsViewModel(
                        repository,
                        productCategory.id
                    ).apply {
                        registerLifecycleOwner(lifecycleOwner)
                    }
                )
            }
        }
    }

    fun onCommand(vm: ViewModel, shared: Any?) {
        clickHandler.first()(productCategory)
        LL.d("$shared")
    }

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }
}