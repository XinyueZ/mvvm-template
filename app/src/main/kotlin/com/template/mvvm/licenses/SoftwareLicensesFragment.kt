package com.template.mvvm.licenses

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.AppBaseFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentSoftwareLicensesBinding
import com.template.mvvm.ext.setupErrorSnackbar
import com.template.mvvm.models.SoftwareLicensesViewModel

class SoftwareLicensesFragment : AppBaseFragment<SoftwareLicensesViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, SoftwareLicensesFragment::class.java.name) as SoftwareLicensesFragment
    }

    private lateinit var binding: FragmentSoftwareLicensesBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentSoftwareLicensesBinding.bind(view)
                .apply {
                    vm = obtainViewModel().apply {
                        registerLifecycleOwner(activity)
                        view.setupErrorSnackbar(this@SoftwareLicensesFragment, this.onError)
                    }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_software_licenses
    override fun requireViewModel() = SoftwareLicensesViewModel::class.java
}