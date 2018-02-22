package com.template.mvvm.app.licenses

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivitySoftwareLicensesBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel

class SoftwareLicensesActivity : AppBaseActivity<SoftwareLicensesViewModel>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_software_licenses

    override fun requireViewModel() = SoftwareLicensesViewModel::class.java
    override fun createViewModelView() = SoftwareLicensesFragment::class.newInstance(application)

    lateinit var binding: ActivitySoftwareLicensesBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySoftwareLicensesBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            setUpActionBar(toolbar)
            vm = obtainViewModel().apply {
                showSystemUi.setupObserve(this@SoftwareLicensesActivity) {
                    execute({ hideSystemUi(1500) }, { showSystemUi() })
                }
                licenseDetailViewModel.setupObserve(this@SoftwareLicensesActivity) {
                    LicenseDetailFragment::class.newInstance(this@SoftwareLicensesActivity)
                        .show(supportFragmentManager, null)
                }
            }
        }
    }
}