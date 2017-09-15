package com.template.mvvm.licenses.list

import android.app.Application
import android.databinding.ObservableField
import com.template.mvvm.data.domain.licenses.Library
import com.template.mvvm.life.LifeViewModel

class SoftwareLicenseItemViewModel(app: Application) : LifeViewModel(app) {

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