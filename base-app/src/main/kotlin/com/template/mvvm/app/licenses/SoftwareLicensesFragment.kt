package com.template.mvvm.app.licenses

import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSoftwareLicensesBinding
import com.template.mvvm.core.models.error.setupErrorSnackbar
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel
import com.template.mvvm.core.models.registerLifecycleOwner

class SoftwareLicensesFragment : AppBaseFragment<SoftwareLicensesViewModel>() {
    override fun onViewCreated(view: View) = FragmentSoftwareLicensesBinding.bind(view).apply {
        vmItem = BR.vm
        vm = obtainViewModel().apply {
            registerLifecycleOwner(activity)
            onError.setupErrorSnackbar(view, activity)
        }
    }

    override fun getLayout() = R.layout.fragment_software_licenses
    override fun requireViewModel() = SoftwareLicensesViewModel::class.java
}