package com.template.mvvm.models

import android.app.Application
import android.arch.lifecycle.*
import android.arch.paging.PagedList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import com.template.mvvm.LL
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.arch.recycler.MvvmListDataProvider
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ext.setUpTransform
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

class SoftwareLicensesViewModel(private val repository: LicensesDataSource) : AbstractViewModel() {

    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

    private val reload = MutableLiveData<Boolean>()
    val dataHaveNotReloaded = ObservableBoolean(true)

    val licenseDetailViewModel = MutableLiveData<LicenseDetailViewModel>()

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()

    // Error
    var onError = ErrorViewModel()

    //Return this view to home
    val goBack = ObservableBoolean(false)

    //Data of this view-model
    private var libraryListSource: LibraryList? = null

    //For recyclerview data
    var libraryItemVmList: ObservableField<LiveData<PagedList<ViewModel>>> = ObservableField()

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        libraryListSource = libraryListSource ?: LibraryList().apply {
            setUpTransform(lifecycleOwner) {
                it?.let {
                    libraryItemVmList.set(
                            MvvmListDataProvider(it).create(
                                    0,
                                    PagedList.Config.Builder()
                                            .setPageSize(it.size)
                                            .setInitialLoadSizeHint(it.size)
                                            .setEnablePlaceholders(true)
                                            .build())
                    )

                    showSystemUi.value = true
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
            launch(UI + vmJob) {
                it.viewModelTapped.consumeEach {
                    // Tell UI to open a UI for license detail.
                    licenseDetailViewModel.value = when (lifecycleOwner) {
                        is Fragment -> {
                            val vm = lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)

                            repository.getLicense(lifecycleOwner.context.applicationContext as Application, vmJob, it, false).consumeEach {
                                LL.d("Show license detail")
                                vm.detail.set(it)

                            }
                            vm
                        }
                        is FragmentActivity -> {
                            val vm = lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                            repository.getLicense(lifecycleOwner.application, vmJob, it, false).consumeEach {
                                LL.d("Show license detail")
                                vm.detail.set(it)
                            }
                            vm
                        }
                        else -> LicenseDetailViewModel()
                    }
                }
            }
        }
    }

    private fun loadAllLicenses(lifecycleOwner: LifecycleOwner, localOnly: Boolean = true) {
        libraryListSource?.let {
            launch(UI + CoroutineExceptionHandler({ _, e ->
                canNotLoadLicenses(e, lifecycleOwner)
                LL.d(e.message ?: "")
            }) + vmJob) {
                repository.getAllLibraries(vmJob, localOnly).consumeEach {
                    LL.i("libraryListSource subscribe")
                    libraryListSource?.value = it
                }
            }
        }
    }

    private fun canNotLoadLicenses(it: Throwable, lifecycleOwner: LifecycleOwner) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            loadAllLicenses(lifecycleOwner, false)
            showSystemUi.value = false

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

    val viewModelTapped = Channel<Library>()

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
        launch(UI + vmJob) {
            viewModelTapped.send(library)
        }
    }

    override fun equals(other: Any?) =
            if (other == null) false
            else TextUtils.equals(library.name, ((other as SoftwareLicenseItemViewModel).library.name))

}




