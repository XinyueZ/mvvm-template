package com.template.mvvm.licenses

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.View
import com.template.mvvm.R
import com.template.mvvm.databinding.FragmentSoftwareLicensesBinding
import com.template.mvvm.life.LifeFragment
import com.template.mvvm.vm.models.SoftwareLicensesViewModel

class SoftwareLicensesFragment : LifeFragment() {
    companion object {
        fun newInstance(cxt: Context) = Fragment.instantiate(cxt, SoftwareLicensesFragment::class.java.name) as SoftwareLicensesFragment
    }

    private lateinit var binding: FragmentSoftwareLicensesBinding

    override fun bindingView(view: View): ViewDataBinding {
        binding = FragmentSoftwareLicensesBinding.bind(view)
                .apply {
                    vm = (obtainViewModel() as SoftwareLicensesViewModel).apply { registerLifecycleOwner(activity as LifecycleOwner) }
                }
        return binding
    }

    override fun getLayout() = R.layout.fragment_software_licenses
}