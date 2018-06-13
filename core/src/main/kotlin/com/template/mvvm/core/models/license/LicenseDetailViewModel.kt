package com.template.mvvm.core.models.license

import androidx.databinding.ObservableField
import com.template.mvvm.core.arch.AbstractViewModel

class LicenseDetailViewModel : AbstractViewModel() {
    val detail = ObservableField<String>()
}