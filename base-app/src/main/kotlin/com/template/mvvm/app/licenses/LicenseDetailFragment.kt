package com.template.mvvm.app.licenses

import android.view.View
import com.template.mvvm.app.AppBaseDialogFragment
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentLicenseDetailBinding
import com.template.mvvm.core.models.license.LicenseDetailViewModel

class LicenseDetailFragment : AppBaseDialogFragment<LicenseDetailViewModel>() {
    override fun bindingView(view: View) =
        FragmentLicenseDetailBinding.bind(view).apply { vm = obtainViewModel() }

    override fun getLayout() = R.layout.fragment_license_detail
    override fun requireViewModel() = LicenseDetailViewModel::class.java
}