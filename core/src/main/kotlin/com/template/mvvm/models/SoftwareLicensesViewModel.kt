package com.template.mvvm.models

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ext.setUpTransform
import io.reactivex.subjects.PublishSubject
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SoftwareLicensesViewModel(private val application: Application, private val repository: LicensesDataSource, val itemBinding: ItemBinding<SoftwareLicenseItemViewModel>) : AbstractViewModel() {

    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

    private val reload = MutableLiveData<Boolean>()
    val dataHaveNotReloaded = ObservableBoolean(true)

    val licenseDetailViewModel = MutableLiveData<LicenseDetailViewModel>()

    // True when the data have been loaded.
    val pageStill = MutableLiveData<Boolean>()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

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
                    dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                    dataHaveNotReloaded.set(true)

                    bindTapHandlers(it, lifecycleOwner)
                }
            }
        }
        reload.observe(lifecycleOwner, Observer {
            loadAllLicenses(lifecycleOwner, false)
        })
        loadAllLicenses(lifecycleOwner)
        return true
    }

    private fun bindTapHandlers(it: List<SoftwareLicenseItemViewModel>, lifecycleOwner: LifecycleOwner) {
        it.forEach {
            addToAutoDispose(it.viewModelTapped.subscribe({
                it?.let {
                    // Tell UI to open a UI for license detail.
                    val vm = lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                    licenseDetailViewModel.value = lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                    addToAutoDispose(repository.getLicense(application, it, false)
                            .subscribe(
                                    { vm.detail.set(it) },
                                    { LL.d(it.message ?: "") }
                            )
                    )
                }
            }, { LL.d(it.message ?: "") }))
        }
    }

    private fun loadAllLicenses(lifecycleOwner: LifecycleOwner, localOnly: Boolean = true) {
        libraryListSource?.let {
            addToAutoDispose(
                    repository.getAllLibraries(localOnly).subscribe(
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
        pageStill.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllLicenses(lifecycleOwner)
            pageStill.value = false

            //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
            libraryListSource?.value?.let {
                dataLoaded.set(it.isNotEmpty())
            } ?: dataLoaded.set(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
        libraryListSource = null
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


