package com.template.mvvm.core.models.license

import android.app.Application
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
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.obtainViewModel
import com.template.mvvm.repository.contract.LicensesDataSource
import com.template.mvvm.repository.domain.licenses.Library
import com.template.mvvm.repository.domain.licenses.LibraryList
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext

class SoftwareLicensesViewModel(
    private val application: Application,
    private val repository: LicensesDataSource
) : AbstractViewModel() {
    val state = SoftwareLicensesViewModelState()
    val controller = SoftwareLicensesViewModelController()

    // Error
    var onError = ErrorViewModel()

    override fun onLifecycleCreate() {
        with(state) {
            with(controller) {
                lifecycleOwner.run {
                    libraryListSource = libraryListSource ?: LibraryList().toViewModelList(
                        this@run,
                        { SoftwareLicenseItemViewModel.newInstance(this, it) }) {
                        it?.let {
                            libraryItemVmList.value = it
                            showSystemUi.value = true
                            dataHaveNotReloaded.set(true)
                            bindTapHandlers(it, this@run)
                        }
                    }

                    libraryItemVmList.apply {
                        value = emptyList()
                    }
                }
            }
        }
    }

    override fun onLifecycleStop() {
        repository.clear()
        controller.libraryListSource = null
    }

    private fun bindTapHandlers(
        it: List<SoftwareLicenseItemViewModel>,
        lifecycleOwner: LifecycleOwner
    ) {
        it.forEach {
            it.clickHandler += {
                controller.licenseDetailViewModel.value =
                        lifecycleOwner.obtainViewModel(LicenseDetailViewModel::class.java)
                            .apply {
                                runBlocking {
                                    repository.getLicense(
                                        application,
                                        bgContext,
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

    private fun loadData() = doOnBound(true)

    private fun reloadData() = doOnBound(false)

    fun onBound(@IntRange(from = 0L) position: Int) {
        if (position < 0) throw IndexOutOfBoundsException("The position must be >= 0")
        when (controller.libraryItemVmList.value?.isEmpty() ?: kotlin.run { true }) {
            true -> loadData()
            else -> controller.libraryListSource?.value = emptyList()
        }
    }

    private fun doOnBound(localOnly: Boolean) = async(uiContext) {
        controller.libraryListSource?.let { source ->
            query(bgContext, localOnly).consumeEach {
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
        with(state) {
            with(controller) {
                showSystemUi.value = true
                dataHaveNotReloaded.set(true)
                onError.value = Error(it, R.string.error_load_all_licenses, R.string.error_retry) {
                    reloadData()
                    showSystemUi.value = false
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

class SoftwareLicenseItemViewModel : AbstractViewModel() {
    lateinit var library: Library
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

    internal val clickHandler = arrayListOf<((Library) -> Unit)>()

    companion object {
        fun newInstance(
            lifecycleOwner: LifecycleOwner,
            library: Library
        ): SoftwareLicenseItemViewModel {
            return SoftwareLicenseItemViewModel().apply {
                this.library = library
                title.set(library.name)
                description.set(library.license.description)
                registerLifecycleOwner(lifecycleOwner)
            }
        }
    }

    fun onCommand(vm: ViewModel, shared: Any?) {
        clickHandler.first()(library)
        LL.d("$shared")
    }

    override fun onLifecycleStop() {
        super.onLifecycleStop()
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
        clickHandler.clear()
    }
}