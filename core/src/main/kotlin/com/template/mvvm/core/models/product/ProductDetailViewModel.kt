package com.template.mvvm.core.models.product

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.databinding.ObservableList
import android.net.Uri
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.repository.LL
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.ProductDetail
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

open class ProductDetailViewModel(private val repository: ProductsDataSource) :
    AbstractViewModel() {

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
    private var productDetailSource: MutableLiveData<ProductDetail>? = null

    var productIdToDetail: Long? = null

    //------------------------------------------------------------------------
    // Fields to update
    //------------------------------------------------------------------------
    val productId = ObservableField<Long>()
    val productTitle = ObservableField<String>()
    val productDescription = ObservableField<String>()
    val productImageUris: ObservableList<Uri> = ObservableArrayList()
    //------------------------------------------------------------------------
    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        assertProduct()
        productDetailSource = productDetailSource ?: (MutableLiveData<ProductDetail>()).apply {
            observe(lifecycleOwner, Observer {
                it?.let {
                    //-----------------------------------------
                    // Update UI with new data here
                    //-----------------------------------------
                    productId.set(it.pid)
                    productTitle.set(it.title)
                    productDescription.set(it.description)
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
        reload.observe(lifecycleOwner, Observer {
            loadAllProducts(lifecycleOwner, false)
        })
        loadAllProducts(lifecycleOwner)
        return true
    }

    protected open fun loadAllProducts(lifecycleOwner: LifecycleOwner, localOnly: Boolean = true) {
        productDetailSource?.let {
            launch(UI + CoroutineExceptionHandler({ _, e ->
                canNotLoadProductDetail(e, lifecycleOwner)
                LL.d(e.message ?: "")
            }) + vmJob) {
                productIdToDetail?.let {
                    repository.getProductDetail(vmJob, productIdToDetail!!, localOnly).consumeEach {
                        LL.i("productDetailSource subscribe")
                        productDetailSource?.value = it
                    }
                } ?: kotlin.run {
                    assertProduct()
                }
            }
        }
    }

    protected fun canNotLoadProductDetail(it: Throwable, lifecycleOwner: LifecycleOwner) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)


        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllProducts(lifecycleOwner, false)
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
        reload.value = true
        dataHaveNotReloaded.set(false)
    }
    //-----------------------------------
}
