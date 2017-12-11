package com.template.mvvm.licenses

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivitySoftwareLicensesBinding
import com.template.mvvm.models.license.SoftwareLicensesViewModel

class SoftwareLicensesActivity : AppBaseActivity<SoftwareLicensesViewModel>() {
    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, SoftwareLicensesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override @LayoutRes
    fun getLayout() = R.layout.activity_software_licenses

    override fun requireViewModel() = SoftwareLicensesViewModel::class.java
    override fun createViewModelView() = SoftwareLicensesFragment.newInstance(application)

    lateinit var binding: ActivitySoftwareLicensesBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySoftwareLicensesBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = obtainViewModel().apply {
            showSystemUi.observe(this@SoftwareLicensesActivity, Observer {
                when (it) {
                    true -> hideSystemUi(1500)
                    false -> showSystemUi()
                }
            })
            licenseDetailViewModel.observe(this@SoftwareLicensesActivity, Observer {
                LicenseDetailFragment.newInstance(this@SoftwareLicensesActivity).show(supportFragmentManager, null)
            })
        }
    }
}