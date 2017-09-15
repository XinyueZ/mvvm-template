package com.template.mvvm.licenses

import android.app.Activity
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivitySoftwareLicensesBinding
import com.template.mvvm.life.LifeActivity

class SoftwareLicensesActivity : LifeActivity() {
    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, SoftwareLicensesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override fun getLayout() = R.layout.activity_software_licenses
    override fun createViewModel() = SoftwareLicensesViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SoftwareLicensesFragment.newInstance(application)) as LifecycleFragment

    lateinit var binding: ActivitySoftwareLicensesBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySoftwareLicensesBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = obtainViewModel() as SoftwareLicensesViewModel
        vm.pageStill.observe(this, Observer { hideSystemUi(2500) })
    }
}