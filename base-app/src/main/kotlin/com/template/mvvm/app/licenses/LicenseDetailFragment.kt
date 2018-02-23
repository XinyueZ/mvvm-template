package com.template.mvvm.app.licenses

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentLicenseDetailBinding
import com.template.mvvm.base.ui.LiveDialogFragment
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.license.LicenseDetailViewModel

class LicenseDetailFragment : LiveDialogFragment() {
    override fun onViewCreated(view: View) =
        FragmentLicenseDetailBinding.bind(view).apply {
            LicenseDetailViewModel::class.generateViewModel(this@LicenseDetailFragment) {
                vm = this
            }
        }

    override fun getLayout() = R.layout.fragment_license_detail
}