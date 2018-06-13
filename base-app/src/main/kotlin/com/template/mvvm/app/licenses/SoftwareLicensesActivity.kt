package com.template.mvvm.app.licenses

import androidx.annotation.LayoutRes
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivitySoftwareLicensesBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.base.ui.LiveActivity
import com.template.mvvm.core.get
import com.template.mvvm.core.models.license.SoftwareLicensesViewModel

class SoftwareLicensesActivity : LiveActivity<ActivitySoftwareLicensesBinding>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_software_licenses

    override fun createLiveFragment() = SoftwareLicensesFragment::class.newInstance(application)
    override fun onCreate(binding: ActivitySoftwareLicensesBinding) {
        setUpActionBar(binding.toolbar)
        SoftwareLicensesViewModel::class.get(this) {
            binding.vm = this
            controller.showSystemUi.setupObserve(this@SoftwareLicensesActivity) {
                execute({ hideSystemUi(1500) }, { showSystemUi() })
            }
            controller.licenseDetailViewModel.setupObserve(this@SoftwareLicensesActivity) {
                LicenseDetailFragment::class.newInstance(this@SoftwareLicensesActivity)
                    .show(supportFragmentManager, null)
            }
        }
    }
}