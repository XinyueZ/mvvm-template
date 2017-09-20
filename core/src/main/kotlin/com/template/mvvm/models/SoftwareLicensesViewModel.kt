package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.databinding.*
import android.view.LayoutInflater
import android.view.ViewGroup
import com.template.mvvm.BR
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.binding.recycler.Binding
import com.template.mvvm.binding.recycler.RecyclerAdapter
import com.template.mvvm.data.source.LicensesRepository
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.ext.switchMapViewModelList

class SoftwareLicensesViewModel(private val repository: LicensesRepository) : AbstractViewModel() {

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
        override fun create(type: Int, inflater: LayoutInflater, parent: ViewGroup) = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_software_license, parent, false)
    }
    val listBinding = object : Binding.OnBind<SoftwareLicenseItemViewModel> {
        override fun onBind(binding: Binding<SoftwareLicenseItemViewModel>, position: Int, data: SoftwareLicenseItemViewModel) {
            binding.set(BR.vm, 0)
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
                            //TODO Error-handling
                        }
                )
        )
        return true
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


