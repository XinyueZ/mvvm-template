package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.BrandList
import com.template.mvvm.ext.setUpTransform
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class AllBrandsViewModel(private val repository: ProductsDataSource, val itemBinding: ItemBinding<BrandItemViewModel>) : AbstractViewModel() {

    // One progress-indicator for loading, only for one.
    val dataLoaded = ObservableBoolean(false)
    // Final loaded of data signal for all available progress-indicators, call notifyChange() on it when needed.
    private val reload = SingleLiveData<Boolean>()
    val dataHaveNotReloaded = ObservableBoolean(true)

    // Error
    var onError = ErrorViewModel()

    //Data of this view-model
    private var brandListSource: BrandList? = null

    //For recyclerview data
    val brandItemVmList = ObservableArrayList<BrandItemViewModel>()

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        brandListSource = brandListSource ?: BrandList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    brandItemVmList.clear()
                    brandItemVmList.addAll(it
                            .filter { !it.isEmpty() }
                            .map {
                                it.itemWidth = itemWidth
                                it.itemHeight = itemHeight
                                it
                            }
                    )
                    dataLoaded.set(true)
                    dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                    dataHaveNotReloaded.set(true)
                }
            }
        }
        reload.observe(lifecycleOwner, Observer {
            loadAllBrands(lifecycleOwner, false)
        })
        loadAllBrands(lifecycleOwner)
        return true
    }

    private fun loadAllBrands(lifecycleOwner: LifecycleOwner, localOnly: Boolean = true) {
        brandListSource?.let {
            launch(vmJob + UI + CoroutineExceptionHandler({ _, e ->
                canNotLoadBrands(e, lifecycleOwner)
                LL.d(e.message ?: "")
            })) {
                repository.getAllBrands(vmJob, localOnly).consumeEach {
                    LL.i("brandListSource subscribe")
                    brandListSource?.value = it
                }
            }
        }
    }

    private fun canNotLoadBrands(it: Throwable, lifecycleOwner: LifecycleOwner) {
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)
        onError.value = Error(it, R.string.error_load_all_brands, R.string.error_retry) {
            loadAllBrands(lifecycleOwner, false)

            //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
            brandListSource?.value?.let {
                dataLoaded.set(it.isNotEmpty())
            } ?: dataLoaded.set(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
        brandListSource = null
    }

    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onReload() {
        reload.value = true
        dataHaveNotReloaded.set(false)
    }
    //-----------------------------------

    var itemWidth: Int = 0
    var itemHeight: Int = 0
}

class BrandItemViewModel : AbstractViewModel() {
    lateinit var brand: Brand
    val logo = ObservableField<Uri>()


    var itemWidth: Int = 0
    var itemHeight: Int = 0

    companion object {
        fun from(brand: Brand): BrandItemViewModel {
            return BrandItemViewModel().apply {
                this.brand = brand
                logo.set(brand.logo)
            }
        }
    }

    fun isEmpty() = brand.logo == Uri.EMPTY

    fun onCommand(vm: ViewModel) {

    }
}

