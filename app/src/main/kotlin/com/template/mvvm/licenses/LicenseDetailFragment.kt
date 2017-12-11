package com.template.mvvm.licenses

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.template.mvvm.AppBaseDialogFragment
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentLicenseDetailBinding
import com.template.mvvm.models.license.LicenseDetailViewModel

class LicenseDetailFragment : AppBaseDialogFragment<LicenseDetailViewModel>() {
    companion object {
        fun newInstance(cxt: Context) = instantiate(cxt, LicenseDetailFragment::class.java.name) as LicenseDetailFragment
    }

    private lateinit var binding: FragmentLicenseDetailBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentLicenseDetailBinding.bind(view)
                .apply { vm = obtainViewModel() }
        return binding
    }

    override fun getLayout() = R.layout.fragment_license_detail
    override fun requireViewModel() = LicenseDetailViewModel::class.java
}