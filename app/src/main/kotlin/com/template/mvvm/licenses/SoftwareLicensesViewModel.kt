package com.template.mvvm.licenses

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.widget.Toast
import com.template.mvvm.R
import com.template.mvvm.data.repository.LicensesRepository
import com.template.mvvm.licenses.list.ListBinding
import com.template.mvvm.licenses.list.ListViewFactory
import com.template.mvvm.licenses.list.SoftwareLicenseItemViewModel
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class SoftwareLicensesViewModel(app: Application, private val repository: LicensesRepository) : LifeViewModel(app) {

    val loadingText = ObservableInt(R.string.loading_software_licenses)
    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)

    //Return this view to home
    val goBack = ObservableBoolean(false)

    fun toggleBack() {
        goBack.set(true)
    }

    // True when the data have been loaded.
    internal val pageStill = SingleLiveData<Boolean>()

    //For recyclerview data
    val libraryList = ObservableArrayList<SoftwareLicenseItemViewModel>()
    val listFactory = ListViewFactory()
    val listBinding = ListBinding()

    override fun registerLifecycleOwner(lifecycleRegistryOwner: LifecycleRegistryOwner): Boolean {
        addToAutoDispose(
                repository.getAllLibraries(lifecycleRegistryOwner).subscribe(
                        {
                            it.switchMapViewModelList(lifecycleRegistryOwner) {
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