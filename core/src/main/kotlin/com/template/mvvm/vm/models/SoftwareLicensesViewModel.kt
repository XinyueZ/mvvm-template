package com.template.mvvm.vm.models

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.databinding.*
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.binding.recycler.Binding
import com.template.mvvm.binding.recycler.RecyclerAdapter
import com.template.mvvm.data.repository.LicensesRepository
import com.template.mvvm.vm.AbstractViewModel
import com.template.mvvm.domain.SingleLiveData
import com.template.mvvm.domain.licenses.Library

class SoftwareLicensesViewModel(app: Application, private val repository: LicensesRepository) : AbstractViewModel(app) {

    val loadingText = ObservableInt(R.string.loading_software_licenses)
    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    // True when the data have been loaded.
    val pageStill = SingleLiveData<Boolean>()

    //For recyclerview data
    val libraryList = ObservableArrayList<SoftwareLicenseItemViewModel>()
    val listFactory = object : RecyclerAdapter.ViewBindingFactory {
        override fun create(type: Int, inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
            val types: Map<Int, Int> = mapOf(0 to R.layout.item_software_license)
            val layout = types[type]
            return DataBindingUtil.inflate<ViewDataBinding>(inflater, layout ?: FALLBACK_LAYOUT, parent, false)
        }
    }
    val listBinding = object : Binding.OnBind<SoftwareLicenseItemViewModel> {
        override fun onBind(binding: Binding<SoftwareLicenseItemViewModel>, position: Int, data: SoftwareLicenseItemViewModel) {
            binding.set(BR.vm, DEFAULT_BINDING_ITEM)
        }
    }

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        addToAutoDispose(
                repository.getAllLibraries(lifecycleOwner).subscribe(
                        {
                            it.switchMapViewModelList(lifecycleOwner) {
                                it?.let {
                                    libraryList.addAll(it)
                                    dataLoaded.set(true)
                                    pageStill.value = true
                                }
                            }
                        },
                        {
                            Toast.makeText(getApplication(), "Cannot load licenses.", Toast.LENGTH_SHORT).show()
                        }
                )
        )
        return true
    }
}

private const val DEFAULT_BINDING_ITEM = 0
private val FALLBACK_LAYOUT = R.layout.item_default

class SoftwareLicenseItemViewModel(app: Application) : AbstractViewModel(app) {

    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

    companion object {
        fun from(app: Application, library: Library): SoftwareLicenseItemViewModel {
            return SoftwareLicenseItemViewModel(app).apply {
                title.set(library.name)
                description.set(library.license.description)
            }
        }
    }

}


