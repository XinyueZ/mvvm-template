package com.template.mvvm.core.models.license

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.annotation.IntRange
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.core.models.AbstractViewModel
import com.template.mvvm.core.models.error.Error
import com.template.mvvm.core.models.error.ErrorViewModel
import com.template.mvvm.core.obtainViewModel
import com.template.mvvm.repository.contract.LicensesDataSource
import com.template.mvvm.repository.domain.licenses.Library
import com.template.mvvm.repository.domain.licenses.LibraryList
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext

class SoftwareLicensesViewModel(
    private val application: Application,
    private val repository: LicensesDataSource
) : AbstractViewModel() {

    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

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
    var libraryItemVmList: MutableLiveData<List<ViewModel>> = SingleLiveData()

    override fun registerLifecycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.run {
            lifecycle.addObserver(this@SoftwareLicensesViewModel)
            libraryListSource = libraryListSource ?: LibraryList().apply {
                setUpTransform(this@run) {
                    it?.let {
                        libraryItemVmList.value = it
                        showSystemUi.value = true
                        dataLoaded.set(true)
                        dataLoaded.notifyChange() // Force for multi UI that will handle this "loaded"
                        dataHaveNotReloaded.set(true)
                        bindTapHandlers(it, this@run)
                    }
                }
            }

            libraryItemVmList.apply {
                removeObservers(this@run)
                value = emptyList()
            }
        }
    }

    private fun bindTapHandlers(
        it: List<SoftwareLicenseItemViewModel>,
        lifecycleOwner: LifecycleOwner
    ) {
        it.forEach {
            it.clickHandler += {
                licenseDetailViewModel.value =
                        lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                            .apply {
                                runBlocking {
                                    repository.getLicense(
                                        application,
                                        CommonPool + vmJob,
                                        it,
                                        false
                                    ).consumeEach {
                                        detail.set(it)
                                    }
                                }
                            }
            }
        }
    }

    private fun loadData() {
        doOnBound(true)
    }

    private fun reloadData() {
        doOnBound(false)
    }

    fun onBound(@IntRange(from = 0L) position: Int) {
        val shouldLoad = libraryItemVmList.value?.isEmpty() ?: kotlin.run { true }
        if (shouldLoad) {
            loadData()
        }
    }

    private fun doOnBound(localOnly: Boolean) = launch(vmUiJob) {
        libraryListSource?.let { source ->
            query(CommonPool + vmJob, localOnly).consumeEach {
                onQueried(source, it)
            }
        }
    }

    private fun onQueried(
        source: LibraryList,
        it: List<Library>?
    ) {
        source.value = it
    }

    private suspend fun query(coroutineContext: CoroutineContext, localOnly: Boolean) =
        repository.getAllLibraries(coroutineContext, localOnly)

    override fun onUiJobError(it: Throwable) {
        showSystemUi.value = true
        dataLoaded.set(true)
        dataHaveNotReloaded.set(true)
        onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
            reloadData()
            showSystemUi.value = false

            //Now reload and should show progress-indicator if there's an empty list or doesn't show when there's a list.
            libraryListSource?.value?.let {
                dataLoaded.set(it.isNotEmpty())
            } ?: dataLoaded.set(false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onLifecycleStop() {
        repository.clear()
        libraryListSource = null
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
            R.id.action_app_bar_indicator -> goBack.set(true)
        }
    }

    fun onReload() {
        reloadData()
        dataHaveNotReloaded.set(false)
    }
    //-----------------------------------
}

class SoftwareLicenseItemViewModel : AbstractViewModel() {
    lateinit var library: Library
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

    val clickHandler = arrayListOf<((Library) -> Unit)>()

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
        clickHandler.first()(library)
    }

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }
}

fun LibraryList.setUpTransform(
    lifecycleOwner: LifecycleOwner,
    body: (t: List<SoftwareLicenseItemViewModel>?) -> Unit
) {
    Transformations.switchMap(this) {
        val itemVmList = arrayListOf<SoftwareLicenseItemViewModel>().apply {
            it.mapTo(this) {
                SoftwareLicenseItemViewModel.from(it)
            }
        }
        SingleLiveData<List<SoftwareLicenseItemViewModel>>()
            .apply {
                value = itemVmList
            }
    }.observe(lifecycleOwner, Observer { body(it) })
}