package com.template.mvvm.app.licenses

import android.view.View
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSoftwareLicensesBinding
import com.template.mvvm.base.ui.LiveFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class SoftwareLicensesFragment : LiveFragment() {
    override fun onViewCreated(view: View) = FragmentSoftwareLicensesBinding.bind(view).apply {
        vmItem = BR.vm
        SoftwareLicensesViewModel::class.get(this@SoftwareLicensesFragment) {
            vm = this
            registerLifecycleOwner(activity)
            onError.setupErrorSnackbar(view, activity)
        }
    }

    override fun getLayout() = R.layout.fragment_software_licenses
}