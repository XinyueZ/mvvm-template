package com.template.mvvm.app.licenses

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.app.AppBaseFragment
import com.template.mvvm.app.BR
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSoftwareLicensesBinding
import com.template.mvvm.base.ext.putObserver
import com.template.mvvm.core.ext.setupErrorSnackbar
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel

class SoftwareLicensesFragment : AppBaseFragment<SoftwareLicensesViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(
            cxt,
            SoftwareLicensesFragment::class.java.name
        ) as SoftwareLicensesFragment
    }

    private lateinit var binding: FragmentSoftwareLicensesBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentSoftwareLicensesBinding.bind(view)
            .apply {
                vmItem = BR.vm
                vm = obtainViewModel().apply {
                    lifecycle.putObserver(this)
                    activity?.let {
                        registerLifecycle(it)
                    }
                    view.setupErrorSnackbar(this@SoftwareLicensesFragment, this.onError)
                }
            }
        return binding
    }

    override fun getLayout() = R.layout.fragment_software_licenses
    override fun requireViewModel() = SoftwareLicensesViewModel::class.java
}