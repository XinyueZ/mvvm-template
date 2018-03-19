package com.template.mvvm.app.licenses

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSoftwareLicensesBinding
import com.template.mvvm.base.ui.ViewModelFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel
import com.template.mvvm.core.arch.registerLifecycleOwner

class SoftwareLicensesFragment : ViewModelFragment<SoftwareLicensesViewModel>() {
    override fun onViewCreated(view: View) = FragmentSoftwareLicensesBinding.bind(view).apply {
        requestViewModel().get(activity) {
            vm = this
            registerLifecycleOwner(activity)
            onError.setupErrorSnackbar(view, activity)
        }
    }

    override fun getLayout() = R.layout.fragment_software_licenses
}