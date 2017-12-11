package com.template.mvvm.models.license

import android.databinding.ObservableField
import com.template.mvvm.models.AbstractViewModel

class LicenseDetailViewModel : AbstractViewModel() {
    val detail = ObservableField<String>()
}