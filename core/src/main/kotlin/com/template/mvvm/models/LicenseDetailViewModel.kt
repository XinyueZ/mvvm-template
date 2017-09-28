package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableField
import com.template.mvvm.domain.licenses.License

class LicenseDetailViewModel : AbstractViewModel() {
    val description = ObservableField<String>()
    internal var license: License = License()
        set(value) {
            field = value
            description.set(value.description)
        }

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        return super.registerLifecycleOwner(lifecycleOwner)
    }
}