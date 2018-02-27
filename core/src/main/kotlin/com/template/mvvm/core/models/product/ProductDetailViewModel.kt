package com.template.mvvm.core.models.product

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.databinding.ObservableList
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.ProductDetail
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlin.coroutines.experimental.CoroutineContext

open class ProductDetailViewModel(private val repository: ProductsDataSource) :
    AbstractViewModel() {

    val title = ObservableInt(R.string.product_list_title)
    val dataLoaded = ObservableBoolean(false)

    val dataHaveNotReloaded = ObservableBoolean(true)

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    //Data of this view-model
    private var productDetailSource: MutableLiveData<ProductDetail>? = null

    var productIdToDetail: Long? = null

    //------------------------------------------------------------------------
    // Fields to update
    //------------------------------------------------------------------------
    val productId = ObservableField<Long>()
    val productTitle = ObservableField<String>()
    val productDescription = ObservableField<Spanned>()
    val productImageUris: ObservableList<Uri> = ObservableArrayList()
    //------------------------------------------------------------------------

    override fun onLifecycleStart() {
        assertProduct()
        productDetailSource = productDetailSource ?: (MutableLiveData<ProductDetail>()).apply {
            observe(lifecycleOwner, Observer {
                it?.let {
                    //-----------------------------------------
                    // Update UI with new data here
                    //-----------------------------------------
                    productId.set(it.pid)
                    productTitle.set(it.title)
                    productDescription.set(it.description.toHtml())
                    productImageUris.clear()
                    productImageUris += it.pictures.map {
                        it.uri
                    }
                    //-----------------------------------------

                    showSystemUi.value = true
                    dataLoaded.set(true)
                    dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                    dataHaveNotReloaded.set(true)
                }
            })
        }

        loadData()
    }

    private fun loadData(localOnly: Boolean = true) = async(uiContext) {
        productDetailSource?.let {
            productIdToDetail?.let {
                query(bgContext, localOnly).consumeEach {
                    productDetailSource?.value = it
                }
            } ?: kotlin.run {
                assertProduct()
            }
        }
    }

    private fun reloadData() {
        loadData(false)
    }

    private suspend fun query(coroutineContext: CoroutineContext, localOnly: Boolean = true) =
        repository.getProductDetail(coroutineContext, productIdToDetail!!, localOnly)

    override fun onUiJobError(it: Throwable) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)


        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadData(false)
            showSystemUi.value = false

            //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
            productDetailSource?.value?.let {
                dataLoaded.set(true)
            } ?: dataLoaded.set(false)
        }
    }

    private fun assertProduct() {
        if (productIdToDetail == null)
            throw NullPointerException("ProductDetailViewModel.product must be inited firstly.")
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
        productIdToDetail = null
        productDetailSource = null
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

fun String.toHtml(trimmed: Boolean = true): Spanned? {
    val result = when {
        isEmpty() -> null
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(
            this,
            0
        )
        else -> Html.fromHtml(this)
    }

    return when (trimmed) {
        true -> result?.trim() as Spanned
        false -> result
    }
}