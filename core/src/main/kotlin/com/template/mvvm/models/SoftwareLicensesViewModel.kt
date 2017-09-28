package com.template.mvvm.models

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ext.setUpTransform
import io.reactivex.subjects.PublishSubject
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SoftwareLicensesViewModel(private val repository: LicensesDataSource, val itemBinding: ItemBinding<SoftwareLicenseItemViewModel>) : AbstractViewModel() {

    val loadingText = ObservableInt(R.string.loading_software_licenses)
    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

    val licenseDetailViewModel = MutableLiveData<LicenseDetailViewModel>()

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

                    it.forEach {
                        addToAutoDispose(it.viewModelTapped.subscribe({
                            it?.let {
                                // Tell UI to open a UI for license detail.
                                licenseDetailViewModel.value = when (lifecycleOwner) {
                                    is Fragment -> {
                                        val vm = lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                                        addToAutoDispose(repository.getLicense(lifecycleOwner.context.applicationContext as Application, it)
                                                .subscribe({ vm.detail.set(it) }, { LL.d(it.message ?: "") }))
                                        vm
                                    }
                                    is FragmentActivity -> {
                                        val vm = lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                                        addToAutoDispose(repository.getLicense(lifecycleOwner.application, it)
                                                .subscribe({ vm.detail.set(it) }, { LL.d(it.message ?: "") }))
                                        vm
                                    }
                                    else -> LicenseDetailViewModel()
                                }
                            }
                        }, { LL.d(it.message ?: "") }))
                    }
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
                            {
                                canNotLoadLicenses(it, lifecycleOwner)
                                LL.d(it.message ?: "")
                            })
            )
        }
    }

    private fun canNotLoadLicenses(it: Throwable, lifecycleOwner: LifecycleOwner) {
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllLicenses(lifecycleOwner)
            pageStill.value = false
            dataLoaded.set(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
        libraryListSource = null
    }
}

class SoftwareLicenseItemViewModel : AbstractViewModel() {
    lateinit var library: Library
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

    val viewModelTapped = PublishSubject.create<Library>()

    companion object {
        fun from(library: Library): SoftwareLicenseItemViewModel {
            return SoftwareLicenseItemViewModel().apply {
                this.library = library
                title.set(library.name)
                description.set(library.license.description)
            }
        }
    }

    fun onCommand(vm: ViewModel) {
        viewModelTapped.onNext(library)
    }
}


