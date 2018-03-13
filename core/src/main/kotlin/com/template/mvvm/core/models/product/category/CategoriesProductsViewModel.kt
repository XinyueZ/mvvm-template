package com.template.mvvm.core.models.product.category

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.support.annotation.IntRange
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.utils.LL
import com.template.mvvm.core.R
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.repository.domain.products.ProductCategory
import com.template.mvvm.repository.domain.products.ProductCategoryList
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

class CategoriesProductsViewModel : AbstractViewModel() {
    val controller = CategoriesProductsViewModelController()
    val state = CategoriesProductsViewModelState()
    // Error
    var onError = ErrorViewModel()
    private var offset: Int = 0

    override fun onLifecycleCreate() {
        with(controller) {
            lifecycleOwner.run {
                productCategoryListSource = productCategoryListSource ?:
                        ProductCategoryList().apply {
                            setUpTransform(this@run) {
                                it?.let {
                                    productCategoryItemVmList.value = it
                                    with(state) {
                                        dataLoaded.set(true)
                                        dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
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
    }

    override fun onLifecycleStop() {
        with(controller) {
            //            repository.clear()
            productCategoryListSource = null
            state.deleteList.set(false)
            offset = 0
        }
    }

    private fun loadData() = onBound(0)

    private fun reloadData() = doReloadData()

    private fun doReloadData() = async(uiContext) {
        //        delete(bgContext).consumeEach {
        offset = 0
        loadData()
//            shouldDeleteList = true
//        }
    }

    fun onBound(@IntRange(from = 0L) position: Int) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        when (controller.productCategoryItemVmList.value?.isEmpty() ?: kotlin.run { true }) {
            true -> doOnBound(position)
        }
    }

    private fun doOnBound(@IntRange(from = 0L) position: Int) = async(uiContext) {
        controller.productCategoryListSource?.let { source ->
            if (position + 1 >= offset) {
                if (offset > 0) {
                    // For progress-loading for more items
                    state.moreLoaded.set(false)
                }
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
                dataLoaded.set(true)
                dataHaveNotReloaded.set(true)
                onError.value = Error(
                    it,
                    R.string.error_load_all_products_categories,
                    R.string.error_retry
                ) {
                    reloadData()

                    //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
                    productCategoryListSource?.value?.let {
                        dataLoaded.set(it.isNotEmpty())
                    } ?: dataLoaded.set(false)
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
    }
    //-----------------------------------
}

class ProductCategoryItemViewModel : AbstractViewModel() {
    lateinit var productCategory: ProductCategory
    val name: ObservableField<String> = ObservableField()
    val clickHandler = arrayListOf<((ProductCategory) -> Unit)>()

    companion object {
        fun from(productCategory: ProductCategory): ProductCategoryItemViewModel {
            return ProductCategoryItemViewModel().apply {
                this.productCategory = productCategory
                name.set(productCategory.name)
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

fun ProductCategoryList.setUpTransform(
    lifecycleOwner: LifecycleOwner,
    body: (t: List<ProductCategoryItemViewModel>?) -> Unit
) {
    Transformations.switchMap(this) {
        val itemVmList = arrayListOf<ProductCategoryItemViewModel>().apply {
            it.mapTo(this) {
                ProductCategoryItemViewModel.from(it)
            }
        }
        SingleLiveData<List<ProductCategoryItemViewModel>>()
            .apply {
                value = itemVmList
            }
    }.setupObserve(lifecycleOwner, { body(this) })
}