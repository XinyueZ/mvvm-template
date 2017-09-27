package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.ext.setUpTransform
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SoftwareLicensesViewModel(private val repository: LicensesDataSource, val itemBinding: ItemBinding<SoftwareLicenseItemViewModel>) : AbstractViewModel() {

    val loadingText = ObservableInt(R.string.loading_software_licenses)
    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

    // True when the data have been loaded.
    val pageStill = MutableLiveData<Boolean>()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    //Data of this view-model
    private var libraryListSource: LibraryList? = null

    //For recyclerview data
    val libraryItemVmList = ObservableArrayList<SoftwareLicenseItemViewModel>()

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        libraryListSource = libraryListSource ?: LibraryList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    libraryItemVmList.clear()
                    libraryItemVmList.addAll(it)
                    pageStill.value = true
                    dataLoaded.set(true)
                }
            }
        }
        loadAllLicenses(lifecycleOwner)
        return true
    }

    private fun loadAllLicenses(lifecycleOwner: LifecycleOwner) {
        libraryListSource?.let {
            addToAutoDispose(
                    repository.getAllLibraries().subscribe(
                            {
                                LL.i("libraryListSource subscribe")
                                libraryListSource?.value = it
                            },
                            { canNotLoadLicenses(it, lifecycleOwner) })
            )
        }
    }

    private fun canNotLoadLicenses(it: Throwable, lifecycleOwner: LifecycleOwner) {
        LL.e(it.toString())
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllLicenses(lifecycleOwner)
            pageStill.value = false
            dataLoaded.set(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        libraryListSource = null
    }
}

class SoftwareLicenseItemViewModel : AbstractViewModel() {

    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

    companion object {
        fun from(library: Library): SoftwareLicenseItemViewModel {
            return SoftwareLicenseItemViewModel().apply {
                title.set(library.name)
                description.set(library.license.description)
            }
        }
    }

}


