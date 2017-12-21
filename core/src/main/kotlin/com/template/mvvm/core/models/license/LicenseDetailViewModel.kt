package com.template.mvvm.core.models.license

import android.databinding.ObservableField
import com.template.mvvm.core.models.AbstractViewModel

class LicenseDetailViewModel : AbstractViewModel() {
    val detail = ObservableField<String>()
}