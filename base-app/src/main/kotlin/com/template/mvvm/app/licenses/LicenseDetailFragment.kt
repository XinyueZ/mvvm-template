package com.template.mvvm.app.licenses

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentLicenseDetailBinding
import com.template.mvvm.base.ui.ViewModelDialogFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.license.LicenseDetailViewModel

class LicenseDetailFragment : ViewModelDialogFragment<LicenseDetailViewModel>() {
    override fun onViewCreated(view: View) =
        FragmentLicenseDetailBinding.bind(view).apply {
            requestViewModel().get(this@LicenseDetailFragment) {
                vm = this
            }
        }

    override fun getLayout() = R.layout.fragment_license_detail
}